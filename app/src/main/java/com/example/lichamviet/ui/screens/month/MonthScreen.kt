package com.example.lichamviet.ui.screens.month

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lichamviet.data.model.SolarDate
import com.example.lichamviet.data.repository.HolidayRepository
import com.example.lichamviet.data.repository.VietCalendarEngine
import com.example.lichamviet.theme.*
import com.example.lichamviet.ui.screens.year.YearScreen
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthScreen(
    currentDate: SolarDate,
    onSelectDayAndGoToBloc: (SolarDate) -> Unit
) {
    var isYearView by remember { mutableStateOf(false) }
    var selectedMonthYear by remember { mutableStateOf(YearMonth.of(currentDate.year, currentDate.month)) }
    var selectedDate by remember { mutableStateOf(currentDate) }

    val today = remember {
        val now = LocalDate.now()
        SolarDate(now.dayOfMonth, now.monthValue, now.year, now.dayOfWeek.value % 7 + 1)
    }

    // Điều hướng vuốt back quay lại từ Lịch Năm
    BackHandler(enabled = isYearView) {
        isYearView = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab chuyển đổi nhanh giữa Lịch Tháng và Lịch Năm
        PrimaryTabRow(
            selectedTabIndex = if (isYearView) 1 else 0,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = !isYearView,
                onClick = { isYearView = false },
                text = { Text("Lịch Tháng", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = isYearView,
                onClick = { isYearView = true },
                text = { Text("Lịch Năm Toàn Cảnh", fontWeight = FontWeight.Bold) }
            )
        }

        if (isYearView) {
            YearScreen(
                currentYear = selectedMonthYear.year,
                onSelectDay = { solar ->
                    onSelectDayAndGoToBloc(solar)
                },
                onSelectMonth = { m, y ->
                    selectedMonthYear = YearMonth.of(y, m)
                    isYearView = false
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                // Month Navigation Header
                MonthHeader(
                    selectedMonthYear = selectedMonthYear,
                    onPrevMonth = { selectedMonthYear = selectedMonthYear.minusMonths(1) },
                    onNextMonth = { selectedMonthYear = selectedMonthYear.plusMonths(1) },
                    onToday = {
                        val now = LocalDate.now()
                        selectedMonthYear = YearMonth.of(now.year, now.monthValue)
                        selectedDate = today
                    }
                )

                // Day of Week Header: T2, T3, T4, T5, T6, T7, CN
                DayOfWeekHeader()

                Spacer(modifier = Modifier.height(4.dp))

                // Month Grid
                CalendarMonthGrid(
                    monthYear = selectedMonthYear,
                    today = today,
                    selectedDate = selectedDate,
                    onDaySelected = { solar -> selectedDate = solar }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selected Day Details Card
                SelectedDayPreviewCard(
                    selectedDate = selectedDate,
                    onViewBloc = { onSelectDayAndGoToBloc(selectedDate) }
                )
            }
        }
    }
}

@Composable
private fun MonthHeader(
    selectedMonthYear: YearMonth,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit
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
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrevMonth) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Tháng trước", tint = MaterialTheme.colorScheme.primary)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Tháng ${selectedMonthYear.monthValue} Năm ${selectedMonthYear.year}",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Lấy tên năm Âm lịch của tháng này
                val lunarMiddle = VietCalendarEngine.convertSolar2Lunar(15, selectedMonthYear.monthValue, selectedMonthYear.year)
                Text(
                    text = "Năm ${lunarMiddle.canChiYear}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToday) {
                    Icon(Icons.Default.Today, contentDescription = "Hôm nay", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onNextMonth) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Tháng sau", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun DayOfWeekHeader() {
    val daysOfWeek = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEachIndexed { index, name ->
            val isSunday = index == 6
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSunday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CalendarMonthGrid(
    monthYear: YearMonth,
    today: SolarDate,
    selectedDate: SolarDate,
    onDaySelected: (SolarDate) -> Unit
) {
    val firstDayOfMonth = monthYear.atDay(1)
    val daysInMonth = monthYear.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 (Mon) to 7 (Sun)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            val totalCells = ((firstDayOfWeek - 1 + daysInMonth + 6) / 7) * 7
            val rows = totalCells / 7

            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNumber = cellIndex - (firstDayOfWeek - 1) + 1

                        if (dayNumber in 1..daysInMonth) {
                            val solar = SolarDate(dayNumber, monthYear.monthValue, monthYear.year, (col + 1) % 7 + 1)
                            val lunar = VietCalendarEngine.convertSolar2Lunar(dayNumber, monthYear.monthValue, monthYear.year)
                            val isSunday = col == 6
                            val isSelected = selectedDate.day == dayNumber && selectedDate.month == monthYear.monthValue && selectedDate.year == monthYear.year
                            val isToday = today.day == dayNumber && today.month == monthYear.monthValue && today.year == monthYear.year

                            DayCell(
                                solar = solar,
                                lunarDay = lunar.day,
                                lunarMonth = lunar.month,
                                isSunday = isSunday,
                                isSelected = isSelected,
                                isToday = isToday,
                                onClick = { onDaySelected(solar) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    solar: SolarDate,
    lunarDay: Int,
    lunarMonth: Int,
    isSunday: Boolean,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isMung1 = lunarDay == 1
    val isRam = lunarDay == 15

    Box(
        modifier = modifier
            .aspectRatio(0.88f)
            .padding(2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    else -> Color.Transparent
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ngày Dương Lịch (To)
            Text(
                text = "${solar.day}",
                fontSize = 15.sp,
                fontWeight = if (isSelected || isToday || isSunday) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isSunday -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            // Ngày Âm Lịch (Nhỏ)
            val lunarText = if (isMung1) "1/${lunarMonth}" else "$lunarDay"
            Text(
                text = lunarText,
                fontSize = 10.sp,
                fontWeight = if (isMung1 || isRam) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    isMung1 || isRam -> MaterialTheme.colorScheme.primary
                    isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun SelectedDayPreviewCard(
    selectedDate: SolarDate,
    onViewBloc: () -> Unit
) {
    val lunar = VietCalendarEngine.convertSolar2Lunar(selectedDate.day, selectedDate.month, selectedDate.year)
    val holidays = HolidayRepository.getHolidaysForDate(selectedDate, lunar)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${selectedDate.dayOfWeekName}, ${selectedDate.day}/${selectedDate.month}/${selectedDate.year}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Âm lịch: Ngày ${lunar.day} ${lunar.monthName} (${lunar.canChiYear})",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (lunar.isAuspiciousDay) AuspiciousGreenContainer else MaterialTheme.colorScheme.errorContainer,
                    border = null
                ) {
                    Text(
                        text = lunar.dayRating,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (lunar.isAuspiciousDay) AuspiciousGreen else MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Can Chi: Ngày ${lunar.canChiDay}, Tháng ${lunar.canChiMonth} • Tiết ${lunar.solarTerm}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (holidays.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = holidays.joinToString(", ") { it.name },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onViewBloc,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Xem Chi Tiết Tờ Lịch Bloc", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
