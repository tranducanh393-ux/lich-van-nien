package com.example.lichamviet.ui.screens.holidays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.WorkOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lichamviet.data.model.Holiday
import com.example.lichamviet.data.model.SolarDate
import com.example.lichamviet.data.repository.HolidayRepository
import com.example.lichamviet.data.repository.VietCalendarEngine
import com.example.lichamviet.theme.*
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.material.icons.filled.EventAvailable

@Composable
fun HolidayScreen() {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Âm lịch, 1: Dương lịch
    var expandedHolidayId by remember { mutableStateOf<String?>(null) }

    val now = remember { LocalDate.now() }
    val nextTetSolar = remember { HolidayRepository.getNextTetSolarDate(now.year, now.monthValue, now.dayOfMonth) }
    val nextTetLunar = remember { VietCalendarEngine.convertSolar2Lunar(nextTetSolar.day, nextTetSolar.month, nextTetSolar.year) }

    // Live countdown timer to Tết
    var durationUntilTet by remember { mutableStateOf(calculateDurationUntilTet(nextTetSolar)) }

    LaunchedEffect(Unit) {
        while (true) {
            durationUntilTet = calculateDurationUntilTet(nextTetSolar)
            delay(1000)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 24.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Hero Card Đếm Ngược Đến Tết Nguyên Đán
        item {
            TetCountdownHeroCard(
                nextTetSolar = nextTetSolar,
                nextTetLunar = nextTetLunar,
                duration = durationUntilTet
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            // Tab chuyển đổi: Lễ Âm Lịch vs Lễ Dương Lịch
            HolidayTypeSelector(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        val holidays = if (selectedTab == 0) HolidayRepository.LUNAR_HOLIDAYS else HolidayRepository.SOLAR_HOLIDAYS

        items(holidays, key = { it.id }) { holiday ->
            HolidayItemCard(
                holiday = holiday,
                isExpanded = expandedHolidayId == holiday.id,
                onToggleExpand = {
                    expandedHolidayId = if (expandedHolidayId == holiday.id) null else holiday.id
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun calculateDurationUntilTet(nextTet: com.example.lichamviet.data.model.SolarDate): Duration {
    val tetDateTime = LocalDateTime.of(nextTet.year, nextTet.month, nextTet.day, 0, 0, 0)
    val nowDateTime = LocalDateTime.now()
    return if (tetDateTime.isAfter(nowDateTime)) {
        Duration.between(nowDateTime, tetDateTime)
    } else {
        Duration.ZERO
    }
}

@Composable
private fun TetCountdownHeroCard(
    nextTetSolar: com.example.lichamviet.data.model.SolarDate,
    nextTetLunar: com.example.lichamviet.data.model.LunarDate,
    duration: Duration
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ĐẾM NGƯỢC ĐÓN TẾT NGUYÊN ĐÁN",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tết ${nextTetLunar.canChiYear} • Mùng 1 (${nextTetSolar.day}/${nextTetSolar.month}/${nextTetSolar.year})",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bộ 4 ô đếm: Ngày - Giờ - Phút - Giây
                val days = duration.toDays()
                val hours = duration.toHoursPart()
                val minutes = duration.toMinutesPart()
                val seconds = duration.toSecondsPart()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CountdownTimeUnit(value = "$days", label = "Ngày")
                    CountdownTimeUnit(value = String.format("%02d", hours), label = "Giờ")
                    CountdownTimeUnit(value = String.format("%02d", minutes), label = "Phút")
                    CountdownTimeUnit(value = String.format("%02d", seconds), label = "Giây")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Chúc Mừng Năm Mới • Vạn Sự Như Ý",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun CountdownTimeUnit(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.25f),
            border = null,
            modifier = Modifier.size(width = 64.dp, height = 54.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
    }
}

@Composable
private fun HolidayTypeSelector(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            label = { Text("Lễ Âm Lịch & Truyền Thống", fontWeight = FontWeight.Bold) },
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            label = { Text("Lễ Dương Lịch & Quốc Khánh", fontWeight = FontWeight.Bold) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HolidayItemCard(
    holiday: Holiday,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onToggleExpand() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge Ngày Tháng
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (holiday.isLunar) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.size(width = 68.dp, height = 54.dp),
                    border = BorderStroke(1.dp, if (holiday.isLunar) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${holiday.day}/${holiday.month}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (holiday.isLunar) "Âm lịch" else "Dương lịch",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Thông tin ngày lễ
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = holiday.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = holiday.subtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (holiday.isNationalDayOff) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.height(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.WorkOff, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Nghỉ lễ toàn quốc", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                IconButton(onClick = onToggleExpand) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Mở rộng",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Phần mở rộng: Ý nghĩa văn hóa & Phong tục tập quán & Nút Thêm vào Lịch Google
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Ý nghĩa truyền thống:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = holiday.description,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 19.sp
                    )

                    if (holiday.customs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Phong tục tập quán đặc trưng:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuspiciousGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        holiday.customs.forEach { custom ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(AuspiciousGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = custom,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nút Thêm vào Lịch Google
                    OutlinedButton(
                        onClick = {
                            val currentYear = LocalDate.now().year
                            val solarTarget = if (holiday.isLunar) {
                                VietCalendarEngine.convertLunar2Solar(holiday.day, holiday.month, currentYear, false)
                            } else {
                                SolarDate(holiday.day, holiday.month, currentYear, 1)
                            }
                            val intent = Intent(Intent.ACTION_INSERT).apply {
                                data = CalendarContract.Events.CONTENT_URI
                                putExtra(CalendarContract.Events.TITLE, holiday.name)
                                putExtra(
                                    CalendarContract.Events.DESCRIPTION,
                                    "${holiday.subtitle}\n${holiday.description}\nPhong tục: ${holiday.customs.joinToString(", ")}"
                                )
                                putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                                val startMillis = LocalDate.of(solarTarget.year, solarTarget.month, solarTarget.day)
                                    .atStartOfDay(ZoneId.systemDefault())
                                    .toInstant()
                                    .toEpochMilli()
                                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + 86400000L)
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventAvailable,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Thêm dịp lễ này vào Lịch Google",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
