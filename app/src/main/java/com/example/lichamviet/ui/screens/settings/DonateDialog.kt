package com.example.lichamviet.ui.screens.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.window.Dialog

data class DonatePackage(
    val id: String,
    val icon: String,
    val name: String,
    val amount: Long,
    val amountDisplay: String,
    val description: String
)

val DONATE_PACKAGES = listOf(
    DonatePackage("tea", "🍵", "Cốc Trà Đá", 10_000, "10.000 đ", "Một lời động viên ấm áp"),
    DonatePackage("coffee", "☕", "Ly Cà Phê", 20_000, "20.000 đ", "Tiếp thêm năng lượng sáng tạo"),
    DonatePackage("lunch", "🍱", "Bữa Trưa", 50_000, "50.000 đ", "Đồng hành bữa ăn vui vẻ"),
    DonatePackage("sponsor", "🌟", "Đồng Hành", 100_000, "100.000 đ", "Chung tay duy trì ứng dụng lâu dài"),
    DonatePackage("gratitude", "💖", "Tri Ân Lớn", 200_000, "200.000 đ", "Ủng hộ sự phát triển vượt bậc"),
    DonatePackage("custom", "✍️", "Tùy Tâm", 0, "Tùy chọn", "Đóng góp số tiền bạn mong muốn")
)

// Cấu hình tài khoản nhận Donate (có thể thay đổi số tài khoản ở đây)
object DonateConfig {
    const val BANK_NAME = "MBBank (Ngân Hàng Quân Đội)"
    const val BANK_CODE = "MB" // Mã ngân hàng Napas
    const val ACCOUNT_NUMBER = "0987654321" // Số tài khoản ngân hàng
    const val ACCOUNT_HOLDER = "TRAN DUC ANH" // Tên chủ tài khoản
    const val MOMO_PHONE = "0987654321" // Số điện thoại MoMo
    const val DEFAULT_TRANSFER_SYNTAX = "Ung ho Lich Am Viet"
}

@Composable
fun DonateDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedPackage by remember { mutableStateOf(DONATE_PACKAGES[1]) } // Mặc định gói Ly Cà Phê (20k)
    var customAmountText by remember { mutableStateOf("30000") }
    var selectedMethodTab by remember { mutableIntStateOf(0) } // 0: Ngân hàng, 1: MoMo, 2: Hướng dẫn QR

    val activeAmount = if (selectedPackage.id == "custom") {
        customAmountText.toLongOrNull() ?: 0L
    } else {
        selectedPackage.amount
    }

    val transferContent = "${DonateConfig.DEFAULT_TRANSFER_SYNTAX} ${selectedPackage.name}"

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Đã sao chép: $text", Toast.LENGTH_SHORT).show()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ỦNG HỘ PHÁT TRIỂN",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Sự ủng hộ của bạn giúp Lịch Vạn Niên duy trì máy chủ và phát triển hoàn toàn thuần khiết, không quảng cáo rác.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Danh mục các gói ủng hộ
                Text(
                    text = "1. CHỌN GÓI ỦNG HỘ (DONATION PACKAGES)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Lưới gói 2 cột
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DONATE_PACKAGES.chunked(2).forEach { rowPackages ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowPackages.forEach { pkg ->
                                val isSelected = selectedPackage.id == pkg.id
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { selectedPackage = pkg },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = pkg.icon, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(
                                                text = pkg.name,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = pkg.amountDisplay,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                            if (rowPackages.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Nhập số tiền tùy tâm nếu chọn custom
                AnimatedVisibility(visible = selectedPackage.id == "custom") {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        OutlinedTextField(
                            value = customAmountText,
                            onValueChange = { customAmountText = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Nhập số tiền muốn đóng góp (VNĐ)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tabs chọn phương thức thanh toán
                Text(
                    text = "2. PHƯƠNG THỨC CHUYỂN KHOẢN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TabRow(
                    selectedTabIndex = selectedMethodTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = selectedMethodTab == 0,
                        onClick = { selectedMethodTab = 0 },
                        text = { Text("Ngân Hàng", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedMethodTab == 1,
                        onClick = { selectedMethodTab = 1 },
                        text = { Text("Ví MoMo", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedMethodTab == 2,
                        onClick = { selectedMethodTab = 2 },
                        text = { Text("Mã VietQR", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nội dung từng Tab phương thức
                when (selectedMethodTab) {
                    0 -> {
                        // Chuyển khoản ngân hàng
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Ngân hàng nhận:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(DonateConfig.BANK_NAME, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Số tài khoản:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(DonateConfig.ACCOUNT_NUMBER, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                    }
                                    FilledTonalButton(
                                        onClick = { copyToClipboard("Số tài khoản", DonateConfig.ACCOUNT_NUMBER) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Chép STK", fontSize = 11.sp)
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                                Column {
                                    Text("Chủ tài khoản:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(DonateConfig.ACCOUNT_HOLDER, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Nội dung chuyển khoản:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(transferContent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    FilledTonalButton(
                                        onClick = { copyToClipboard("Nội dung chuyển khoản", transferContent) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Chép", fontSize = 11.sp)
                                    }
                                }

                                if (activeAmount > 0) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Số tiền:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("%,d đ".format(activeAmount), fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        // Ví MoMo
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFA50064)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("M", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Chuyển qua Ví MoMo", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Số điện thoại MoMo:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(DonateConfig.MOMO_PHONE, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                    }
                                    FilledTonalButton(
                                        onClick = { copyToClipboard("Số điện thoại MoMo", DonateConfig.MOMO_PHONE) },
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Chép SĐT", fontSize = 11.sp)
                                    }
                                }

                                Column {
                                    Text("Chủ ví:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(DonateConfig.ACCOUNT_HOLDER, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    2 -> {
                        // Hướng dẫn VietQR
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Quét Mã VietQR Chuyển Nhanh 24/7",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Mở ứng dụng Ngân hàng bất kỳ (Vietcombank, MB, Techcombank, VPBank...) chọn chức năng Quét QR và chuyển khoản vào số tài khoản:\n${DonateConfig.ACCOUNT_NUMBER} (${DonateConfig.BANK_NAME})",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { copyToClipboard("Số tài khoản", DonateConfig.ACCOUNT_NUMBER) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Sao Chép Số Tài Khoản Ngân Hàng")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Lời cảm ơn & Nút đóng
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trân trọng cảm ơn bạn! ❤️",
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = onDismiss) {
                        Text("Đóng Hộp Thoại", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
