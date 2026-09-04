package com.example.lichamviet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lichamviet.data.model.SolarDate
import com.example.lichamviet.data.repository.VietCalendarEngine
import com.example.lichamviet.ui.components.AppBottomBar
import com.example.lichamviet.ui.components.AppTab
import com.example.lichamviet.ui.screens.day.DayScreen
import com.example.lichamviet.ui.screens.holidays.HolidayScreen
import com.example.lichamviet.ui.screens.month.MonthScreen
import com.example.lichamviet.ui.screens.settings.SettingsScreen
import com.example.lichamviet.ui.screens.utilities.UtilitiesScreen
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    var currentTab by remember { mutableStateOf(AppTab.DAY) }
    var isShowingSettings by remember { mutableStateOf(false) }

    val today = remember {
        val now = LocalDate.now()
        SolarDate(now.dayOfMonth, now.monthValue, now.year, now.dayOfWeek.value % 7 + 1)
    }
    var currentDate by remember { mutableStateOf(today) }

    val currentLunar = remember(currentDate) {
        VietCalendarEngine.convertSolar2Lunar(currentDate.day, currentDate.month, currentDate.year)
    }

    // Xử lý điều hướng vuốt back / phím back
    BackHandler(enabled = isShowingSettings) {
        isShowingSettings = false
    }

    if (isShowingSettings) {
        SettingsScreen(
            onBack = { isShowingSettings = false }
        )
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "LỊCH VẠN NIÊN",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "Năm ${currentLunar.canChiYear}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { isShowingSettings = true }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Cài đặt",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
            bottomBar = {
                AppBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentTab) {
                    AppTab.DAY -> DayScreen(
                        currentDate = currentDate,
                        onDateChange = { currentDate = it }
                    )
                    AppTab.MONTH -> MonthScreen(
                        currentDate = currentDate,
                        onSelectDayAndGoToBloc = { solar ->
                            currentDate = solar
                            currentTab = AppTab.DAY
                        }
                    )
                    AppTab.HOLIDAYS -> HolidayScreen()
                    AppTab.UTILITIES -> UtilitiesScreen()
                }
            }
        }
    }
}
