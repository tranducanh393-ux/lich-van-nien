@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.lichamviet.ui.screens.utilities

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lichamviet.data.model.LunarDate
import com.example.lichamviet.data.model.SolarDate
import com.example.lichamviet.data.model.VanKhan
import com.example.lichamviet.data.repository.CalendarSyncHelper
import com.example.lichamviet.data.repository.VanKhanRepository
import com.example.lichamviet.data.repository.VietCalendarEngine
import com.example.lichamviet.theme.*
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilitiesScreen() {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Văn Khấn, 1: Đổi Ngày, 2: Giờ Hoàng Đạo, 3: Đồng Bộ Google
    var readingVanKhan by remember { mutableStateOf<VanKhan?>(null) }

    // Hỗ trợ điều hướng vuốt back khi đang đọc văn khấn
    BackHandler(enabled = readingVanKhan != null) {
        readingVanKhan = null
    }

    if (readingVanKhan != null) {
        VanKhanDetailView(
            vanKhan = readingVanKhan!!,
            onBack = { readingVanKhan = null }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Thanh chuyển đổi tính năng dạng cuộn tiện lợi
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 16.dp
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Văn Khấn", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Đổi Ngày", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Giờ Hoàng Đạo & Xuất Hành", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Đồng Bộ Lịch Google", fontWeight = FontWeight.Bold) }
                )
            }

            when (selectedTab) {
                0 -> VanKhanListView(onSelectVanKhan = { readingVanKhan = it })
                1 -> DateConverterView()
                2 -> ZodiacTravelView()
                3 -> GoogleCalendarSyncView()
            }
        }
    }
}

@Composable
private fun VanKhanListView(onSelectVanKhan: (VanKhan) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tất cả") }

    val categories = listOf("Tất cả", "Tết & Giao Thừa", "Mùng 1 & Rằm", "Đền Chùa - Thần Linh")

    val filteredList = remember(searchQuery, selectedCategory) {
        VanKhanRepository.VAN_KHAN_LIST.filter { item ->
            val matchCategory = selectedCategory == "Tất cả" || item.category == selectedCategory
            val matchSearch = searchQuery.isBlank() || item.title.contains(searchQuery, ignoreCase = true) || item.occasion.contains(searchQuery, ignoreCase = true)
            matchCategory && matchSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tìm kiếm bài văn khấn, dịp cúng...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Xóa")
                    }
                }
            },
            shape = CircleShape,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat, fontSize = 12.sp) },
                    shape = CircleShape
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredList) { item ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectVanKhan(item) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = item.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.occasion,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VanKhanDetailView(vanKhan: VanKhan, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = vanKhan.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Ý NGHĨA & DỊP CÚNG",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = vanKhan.occasion,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "NỘI DUNG VĂN KHẤN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = vanKhan.content,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(18.dp)
                )
            }
        }
    }
}

@Composable
private fun DateConverterView() {
    var isSolarToLunar by remember { mutableStateOf(true) }

    var inputDay by remember { mutableStateOf(LocalDate.now().dayOfMonth.toString()) }
    var inputMonth by remember { mutableStateOf(LocalDate.now().monthValue.toString()) }
    var inputYear by remember { mutableStateOf(LocalDate.now().year.toString()) }

    var convertedLunar by remember { mutableStateOf<LunarDate?>(null) }
    var convertedSolar by remember { mutableStateOf<SolarDate?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSolarToLunar) "Dương Lịch ➔ Âm Lịch" else "Âm Lịch ➔ Dương Lịch",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = {
                        isSolarToLunar = !isSolarToLunar
                        convertedLunar = null
                        convertedSolar = null
                        errorMessage = null
                    }) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = "Đổi chiều")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputDay,
                        onValueChange = { inputDay = it },
                        label = { Text("Ngày") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = inputMonth,
                        onValueChange = { inputMonth = it },
                        label = { Text("Tháng") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = inputYear,
                        onValueChange = { inputYear = it },
                        label = { Text("Năm") },
                        modifier = Modifier.weight(1.3f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        errorMessage = null
                        val d = inputDay.toIntOrNull()
                        val m = inputMonth.toIntOrNull()
                        val y = inputYear.toIntOrNull()

                        if (d == null || m == null || y == null || d !in 1..31 || m !in 1..12 || y !in 1900..2199) {
                            errorMessage = "Vui lòng nhập ngày, tháng, năm hợp lệ (Năm từ 1900 - 2199)"
                            return@Button
                        }

                        if (isSolarToLunar) {
                            convertedLunar = VietCalendarEngine.convertSolar2Lunar(d, m, y)
                            convertedSolar = null
                        } else {
                            // Chuyển âm sang dương
                            for (tryDay in 1..366) {
                                val testDate = LocalDate.of(y, 1, 1).plusDays(tryDay.toLong() - 1)
                                val l = VietCalendarEngine.convertSolar2Lunar(testDate.dayOfMonth, testDate.monthValue, testDate.year)
                                if (l.day == d && l.month == m) {
                                    convertedSolar = SolarDate(testDate.dayOfMonth, testDate.monthValue, testDate.year, testDate.dayOfWeek.value % 7 + 1)
                                    convertedLunar = null
                                    return@Button
                                }
                            }
                            errorMessage = "Không tìm thấy ngày dương tương ứng cho tháng âm này"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape
                ) {
                    Text("Tra Cứu & Chuyển Đổi", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (convertedLunar != null) {
            val lunar = convertedLunar!!
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "KẾT QUẢ ÂM LỊCH",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ngày ${lunar.day} ${lunar.monthName}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Năm ${lunar.canChiYear}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Can Chi Ngày: Ngày ${lunar.canChiDay} • Tiết ${lunar.solarTerm}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        if (convertedSolar != null) {
            val solar = convertedSolar!!
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "KẾT QUẢ DƯƠNG LỊCH",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${solar.dayOfWeekName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "${solar.day} Tháng ${solar.month} Năm ${solar.year}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ZodiacTravelView() {
    val today = remember { LocalDate.now() }
    val lunar = remember(today) {
        VietCalendarEngine.convertSolar2Lunar(today.dayOfMonth, today.monthValue, today.year)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Thẻ Tổng quan Ngày
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "HÔM NAY: ${today.dayOfMonth}/${today.monthValue}/${today.year}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Ngày ${lunar.canChiDay}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = if (lunar.isAuspiciousDay) Color(0xFF2E7D32).copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            text = lunar.dayRating,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (lunar.isAuspiciousDay) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Hướng xuất hành
                Text(
                    text = "HƯỚNG XUẤT HÀNH ĐẮC LỘC",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Hỷ Thần (May Mắn)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(lunar.hyThanDirection, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Tài Thần (Tài Lộc)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(lunar.taiThanDirection, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách 12 giờ hoàng đạo
        Text(
            text = "12 GIỜ TRONG NGÀY (HOÀNG ĐẠO / HẮC ĐẠO)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        lunar.auspiciousHours.forEach { hour ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = if (hour.isAuspicious) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Giờ ${hour.name}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${hour.timeRange})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Sao: ${hour.starName}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (hour.isAuspicious) Color(0xFF2E7D32).copy(alpha = 0.15f) else Color(0xFF757575).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (hour.isAuspicious) "Hoàng Đạo" else "Hắc Đạo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hour.isAuspicious) Color(0xFF2E7D32) else Color(0xFF757575),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoogleCalendarSyncView() {
    val context = LocalContext.current
    val today = remember { LocalDate.now() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4285F4)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudSync,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Đồng Bộ Sang Google Calendar",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Thêm nhắc nhở các dịp âm lịch quan trọng trực tiếp vào ứng dụng Google Calendar trên thiết bị của bạn hoàn toàn miễn phí và không cần đăng nhập.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        CalendarSyncHelper.syncNextSocVongToCalendar(context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.AddAlert, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thêm Ngày Sóc Vọng (Mùng 1 & Rằm)", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        CalendarSyncHelper.addEventToCalendar(
                            context = context,
                            title = "Tết Nguyên Đán (Mùng 1 Tết Âm Lịch)",
                            description = "Ngày đầu năm mới âm lịch cổ truyền. Chúc mừng năm mới an khang thịnh vượng! Tự động tạo bởi Lịch Âm Việt.",
                            solarDate = LocalDate.of(today.year + (if (today.monthValue >= 3) 1 else 0), 2, 17)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Celebration, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thêm Tết Nguyên Đán vào Lịch", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                FilledTonalButton(
                    onClick = {
                        CalendarSyncHelper.openCalendarApp(context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mở Ứng Dụng Lịch Google Trên Máy")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Thẻ chia sẻ nhanh
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CHIA SẺ LỊCH CHO NGƯỜI THÂN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gửi thông tin ngày âm, giờ hoàng đạo và hướng xuất hành hôm nay qua Zalo, Messenger hoặc SMS.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        CalendarSyncHelper.shareDayInfo(context, today)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Chia Sẻ Ngay", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
