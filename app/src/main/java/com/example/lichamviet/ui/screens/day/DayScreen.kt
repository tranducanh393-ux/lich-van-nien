package com.example.lichamviet.ui.screens.day

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lichamviet.data.model.SolarDate
import com.example.lichamviet.data.repository.HolidayRepository
import com.example.lichamviet.data.repository.QuotesRepository
import com.example.lichamviet.data.repository.VietCalendarEngine
import com.example.lichamviet.theme.*
import java.time.LocalDate
import java.time.ZoneId
import android.content.Intent
import android.provider.CalendarContract

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.ui.input.pointer.pointerInput
import com.example.lichamviet.data.repository.DailyGraphicsRepository
import com.example.lichamviet.data.repository.HolidayArtBanner
import com.example.lichamviet.data.repository.HolidayArtRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScreen(
    currentDate: SolarDate,
    onDateChange: (SolarDate) -> Unit
) {
    val lunarDate = VietCalendarEngine.convertSolar2Lunar(currentDate.day, currentDate.month, currentDate.year)
    val holidays = HolidayRepository.getHolidaysForDate(currentDate, lunarDate)
    val quote = QuotesRepository.getQuoteForDay(currentDate.day + currentDate.month * 31)

    val today = remember {
        val now = LocalDate.now()
        SolarDate(now.dayOfMonth, now.monthValue, now.year, now.dayOfWeek.value % 7 + 1)
    }
    var showDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val artBanner = remember(currentDate, lunarDate) {
        HolidayArtRepository.getArtBannerForDate(currentDate, lunarDate, holidays)
    }

    val onAddToGoogleCalendar: () -> Unit = {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            val title = if (holidays.isNotEmpty()) {
                "${holidays.first().name} (ÂL: ${lunarDate.day}/${lunarDate.month} ${lunarDate.canChiDay})"
            } else {
                "Lịch Âm: Ngày ${currentDate.day}/${currentDate.month} (ÂL: ${lunarDate.day}/${lunarDate.month} ${lunarDate.canChiDay})"
            }
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(
                CalendarContract.Events.DESCRIPTION,
                "Âm lịch: Ngày ${lunarDate.day} ${lunarDate.monthName} năm ${lunarDate.canChiYear}\n" +
                "Can Chi: Ngày ${lunarDate.canChiDay}, Tháng ${lunarDate.canChiMonth}, Năm ${lunarDate.canChiYear}\n" +
                "Đánh giá: Ngày ${lunarDate.dayRating} • Tiết ${lunarDate.solarTerm}\n" +
                "Hướng xuất hành: Hỷ Thần (${lunarDate.hyThanDirection}), Tài Thần (${lunarDate.taiThanDirection})\n" +
                "Giờ Hoàng Đạo: ${lunarDate.auspiciousHours.filter { it.isAuspicious }.joinToString(", ") { it.name }}"
            )
            putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
            val startMillis = LocalDate.of(currentDate.year, currentDate.month, currentDate.day)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + 86400000L)
        }
        context.startActivity(intent)
    }

    val onPrev = {
        val prev = LocalDate.of(currentDate.year, currentDate.month, currentDate.day).minusDays(1)
        onDateChange(SolarDate(prev.dayOfMonth, prev.monthValue, prev.year, prev.dayOfWeek.value % 7 + 1))
    }
    val onNext = {
        val next = LocalDate.of(currentDate.year, currentDate.month, currentDate.day).plusDays(1)
        onDateChange(SolarDate(next.dayOfMonth, next.monthValue, next.year, next.dayOfWeek.value % 7 + 1))
    }

    var dragOffsetX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(currentDate) {
                detectHorizontalDragGestures(
                    onDragStart = { dragOffsetX = 0f },
                    onDragEnd = {
                        if (dragOffsetX > 80f) {
                            // Vuốt sang phải -> về ngày hôm trước
                            onPrev()
                        } else if (dragOffsetX < -80f) {
                            // Vuốt sang trái -> sang ngày tiếp theo
                            onNext()
                        }
                        dragOffsetX = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        dragOffsetX += dragAmount
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Thanh điều hướng ngày: Tháng/Năm, Nút Hôm nay, Mũi tên tới/lui
            DayScreenHeader(
                currentDate = currentDate,
                onPrevDay = onPrev,
                onNextDay = onNext,
                onToday = {
                    onDateChange(today)
                },
                onOpenDatePicker = { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Thẻ Nghệ Thuật Lễ Tết & Ngữ Cảnh Ngày
            HolidayArtBannerCard(banner = artBanner)

            Spacer(modifier = Modifier.height(14.dp))

            // Tờ Bloc Lịch Truyền Thống
            BlocCalendarCard(
                solarDate = currentDate,
                lunarDate = lunarDate,
                holidays = holidays,
                onAddToGoogleCalendar = onAddToGoogleCalendar
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Đồ họa Linh vật 12 con giáp (Mão là Mèo thuần Việt)
            ZodiacAnimalCard(canChiDay = lunarDate.canChiDay)

            Spacer(modifier = Modifier.height(14.dp))

            // Đồ họa Chu kỳ Tuần trăng thiên văn
            MoonPhaseCard(lunarDay = lunarDate.day)

            Spacer(modifier = Modifier.height(14.dp))

            // Thẻ Can Chi & Hướng xuất hành
            CanChiCard(lunarDate = lunarDate)

            Spacer(modifier = Modifier.height(14.dp))

            // Thẻ Giờ Hoàng Đạo trong ngày
            ZodiacHoursCard(lunarDate = lunarDate)

            Spacer(modifier = Modifier.height(14.dp))

            // Thẻ Ca dao, Tục ngữ Việt Nam
            QuoteCard(quote = quote)
        }

        // Material 3 Extended FAB "Về Hôm Nay"
        val isNotToday = currentDate.day != today.day || currentDate.month != today.month || currentDate.year != today.year
        AnimatedVisibility(
            visible = isNotToday,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            ExtendedFloatingActionButton(
                onClick = { onDateChange(today) },
                icon = { Icon(Icons.Default.Today, contentDescription = null) },
                text = { Text("Về Hôm Nay", fontWeight = FontWeight.Bold) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        }
    }

    // Material 3 DatePickerDialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.of(currentDate.year, currentDate.month, currentDate.day)
                .atStartOfDay(java.time.ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selected = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneOffset.UTC)
                                .toLocalDate()
                            onDateChange(SolarDate(selected.dayOfMonth, selected.monthValue, selected.year, selected.dayOfWeek.value % 7 + 1))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Chọn", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Đóng")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun DayScreenHeader(
    currentDate: SolarDate,
    onPrevDay: () -> Unit,
    onNextDay: () -> Unit,
    onToday: () -> Unit,
    onOpenDatePicker: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrevDay) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Ngày trước", tint = MaterialTheme.colorScheme.primary)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onOpenDatePicker() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tháng ${currentDate.month} Năm ${currentDate.year}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Chọn ngày",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = currentDate.dayOfWeekName,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToday) {
                    Icon(Icons.Default.Today, contentDescription = "Hôm nay", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onNextDay) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Ngày sau", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun HolidayArtBannerCard(banner: HolidayArtBanner) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (banner.isMajorFestival) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (banner.isMajorFestival) 4.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = banner.emojiBig, fontSize = 26.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = banner.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (banner.isMajorFestival) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = banner.subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (banner.isMajorFestival) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = banner.description,
                fontSize = 12.sp,
                color = if (banner.isMajorFestival) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                banner.tags.take(4).forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BlocCalendarCard(
    solarDate: SolarDate,
    lunarDate: com.example.lichamviet.data.model.LunarDate,
    holidays: List<com.example.lichamviet.data.model.Holiday>,
    onAddToGoogleCalendar: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Phần đầu bloc lịch thanh lịch chuẩn Material 3
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "THÁNG ${solarDate.month} • NĂM ${solarDate.year}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Thứ trong tuần
            Text(
                text = solarDate.dayOfWeekName.uppercase(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (solarDate.isSunday) SundayRed else MaterialTheme.colorScheme.onSurface,
                letterSpacing = 1.sp
            )

            // Số Ngày Dương Lịch To
            Text(
                text = "${solarDate.day}",
                fontSize = 92.sp,
                fontWeight = FontWeight.Black,
                color = if (solarDate.isSunday) SundayRed else MaterialTheme.colorScheme.onSurface,
                letterSpacing = (-3).sp,
                lineHeight = 92.sp
            )

            // Ngày Lễ Dương/Âm Lịch nếu có
            if (holidays.isNotEmpty()) {
                holidays.forEach { holiday ->
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = holiday.name,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Khung Âm Lịch Trang Trọng
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ÂM LỊCH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Ngày ${lunarDate.day}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "•",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lunarDate.monthName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Năm ${lunarDate.canChiYear}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Badges: Hoàng Đạo & Tiết Khí
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Badge Hoàng Đạo / Hắc Đạo
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (lunarDate.isAuspiciousDay) AuspiciousGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                    border = null
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (lunarDate.isAuspiciousDay) AuspiciousGreen else MaterialTheme.colorScheme.error)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ngày ${lunarDate.dayRating}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (lunarDate.isAuspiciousDay) AuspiciousGreen else MaterialTheme.colorScheme.error
                        )
                    }
                }

                // Badge Tiết Khí
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = null
                ) {
                    Text(
                        text = "Tiết ${lunarDate.solarTerm}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Nút Thêm vào Lịch Google / Thiết bị
            OutlinedButton(
                onClick = onAddToGoogleCalendar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.EventAvailable,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Thêm vào Lịch Google / Thiết bị",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun CanChiCard(lunarDate: com.example.lichamviet.data.model.LunarDate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "THÔNG TIN CAN CHI",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CanChiColumn("Ngày", lunarDate.canChiDay)
                CanChiColumn("Tháng", lunarDate.canChiMonth)
                CanChiColumn("Năm", lunarDate.canChiYear)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Xuất hành: Hỷ Thần (${lunarDate.hyThanDirection})",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Tài Thần: ${lunarDate.taiThanDirection}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun CanChiColumn(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun ZodiacHoursCard(lunarDate: com.example.lichamviet.data.model.LunarDate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GIỜ HOÀNG ĐẠO (6 KHUNG GIỜ)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuspiciousGreen,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Danh sách các giờ Hoàng Đạo (màu xanh ngọc)
            val auspicious = lunarDate.auspiciousHours.filter { it.isAuspicious }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                auspicious.chunked(2).forEach { rowList ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowList.forEach { hour ->
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = AuspiciousGreenContainer,
                                border = null
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(AuspiciousGreen)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "${hour.name} (${hour.starName})",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AuspiciousGreen
                                        )
                                        Text(
                                            text = hour.timeRange,
                                            fontSize = 11.sp,
                                            color = AuspiciousGreen.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                        if (rowList.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuoteCard(quote: Pair<String, String>) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CA DAO & TỤC NGỮ VIỆT NAM",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "“${quote.first}”",
                fontSize = 14.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "— ${quote.second} —",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ZodiacAnimalCard(canChiDay: String) {
    val animal = remember(canChiDay) { DailyGraphicsRepository.getZodiacAnimalForDay(canChiDay) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = animal.iconEmoji,
                    fontSize = 28.sp
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Linh Vật Ngày: ${animal.vietnameseAnimal} (${animal.chi})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = animal.symbolism,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = animal.auspiciousNote,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun MoonPhaseCard(lunarDay: Int) {
    val moonPhase = remember(lunarDay) { DailyGraphicsRepository.getMoonPhaseForDay(lunarDay) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF263238)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = moonPhase.iconEmoji,
                    fontSize = 28.sp
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = moonPhase.phaseName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = moonPhase.phaseDetail,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = moonPhase.culturalNote,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
