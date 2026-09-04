package com.example.lichamviet.ui.screens.year

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
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
import com.example.lichamviet.data.repository.VietCalendarEngine
import com.example.lichamviet.theme.*
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearScreen(
    currentYear: Int,
    onSelectDay: (SolarDate) -> Unit,
    onSelectMonth: (Int, Int) -> Unit
) {
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    val today = remember { LocalDate.now() }

    val lunarYearName = remember(selectedYear) {
        val midYearLunar = VietCalendarEngine.convertSolar2Lunar(15, 6, selectedYear)
        midYearLunar.canChiYear
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Year Navigation Header
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { selectedYear-- }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Năm trước", tint = MaterialTheme.colorScheme.primary)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "NĂM $selectedYear",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Năm Âm Lịch: $lunarYearName",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { selectedYear = today.year }) {
                        Icon(Icons.Default.Today, contentDescription = "Năm nay", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { selectedYear++ }) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "Năm sau", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // 12 Months Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items((1..12).toList()) { month ->
                MiniMonthCard(
                    year = selectedYear,
                    month = month,
                    today = today,
                    onSelectMonth = { onSelectMonth(month, selectedYear) },
                    onSelectDay = onSelectDay
                )
            }
        }
    }
}

@Composable
private fun MiniMonthCard(
    year: Int,
    month: Int,
    today: LocalDate,
    onSelectMonth: () -> Unit,
    onSelectDay: (SolarDate) -> Unit
) {
    val yearMonth = remember(year, month) { YearMonth.of(year, month) }
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value // 1 (Mon) to 7 (Sun)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelectMonth() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Tháng
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tháng $month",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Chạm để xem chi tiết →",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Thứ 2..Chủ Nhật
            val daysOfWeek = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysOfWeek.forEachIndexed { index, name ->
                    Text(
                        text = name,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (index == 6) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(6.dp))

            // Calendar Days Matrix
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
                            val isSunday = col == 6
                            val isToday = today.year == year && today.monthValue == month && today.dayOfMonth == dayNumber

                            // Tính ngày âm
                            val lunar = VietCalendarEngine.convertSolar2Lunar(dayNumber, month, year)
                            val isMung1OrRam = lunar.day == 1 || lunar.day == 15

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isToday -> MaterialTheme.colorScheme.primaryContainer
                                            isMung1OrRam -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable {
                                        onSelectDay(SolarDate(dayNumber, month, year, (col + 1) % 7 + 1))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$dayNumber",
                                        fontSize = 12.sp,
                                        fontWeight = if (isToday || isSunday) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isToday -> MaterialTheme.colorScheme.primary
                                            isSunday -> SundayRed
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                    if (isMung1OrRam) {
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .clip(CircleShape)
                                                .background(if (lunar.day == 15) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
