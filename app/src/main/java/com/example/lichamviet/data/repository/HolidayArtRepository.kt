package com.example.lichamviet.data.repository

import com.example.lichamviet.data.model.Holiday
import com.example.lichamviet.data.model.LunarDate
import com.example.lichamviet.data.model.SolarDate

data class HolidayArtBanner(
    val title: String,
    val subtitle: String,
    val emojiBig: String,
    val description: String,
    val tags: List<String>,
    val isMajorFestival: Boolean = false
)

object HolidayArtRepository {

    fun getArtBannerForDate(
        solarDate: SolarDate,
        lunarDate: LunarDate,
        holidays: List<Holiday>
    ): HolidayArtBanner {
        // 1. Tết Nguyên Đán (Mùng 1 - Mùng 5 Tết)
        if (lunarDate.month == 1 && lunarDate.day in 1..5) {
            val dayName = when (lunarDate.day) {
                1 -> "Mùng 1 Tết (Tết Cha • Đầu Năm Xuất Hành)"
                2 -> "Mùng 2 Tết (Tết Mẹ • Thăm Quê Ngoại)"
                3 -> "Mùng 3 Tết (Tết Thầy • Tri Ân Sư Phụ)"
                4 -> "Mùng 4 Tết (Lễ Hóa Vàng Tống Tổ)"
                else -> "Mùng 5 Tết (Khai Hạ • Khởi Đầu Mới)"
            }
            return HolidayArtBanner(
                title = "🌸 CUNG CHÚC TÂN XUÂN • NĂM ${lunarDate.canChiYear.uppercase()}",
                subtitle = dayName,
                emojiBig = "🧧",
                description = "Hoa đào hoa mai nở rộ, bánh chưng xanh, mâm ngũ quả sum vầy. Kính chúc gia đạo vạn sự như ý, tấn tài tấn lộc, phúc thọ an khang!",
                tags = listOf("🌸 Hoa Đào", "🧧 Bao Lì Xì", "🍲 Bánh Chưng", "🎍 Cây Nêu", "🦁 Múa Lân"),
                isMajorFestival = true
            )
        }

        // 2. Đêm Giao Thừa & Tất Niên (29/30 tháng Chạp)
        if (lunarDate.month == 12 && lunarDate.day >= 29) {
            return HolidayArtBanner(
                title = "🎆 THỜI KHẮC GIAO THỪA THIÊNG LIÊNG",
                subtitle = "Tất Niên & Chuyển Giao Năm Cũ - Năm Mới",
                emojiBig = "🎆",
                description = "Phút giây trời đất giao hòa, khép lại mọi nhọc nhằn năm cũ, chuẩn bị đón thời khắc trừ tịch và nguồn sinh khí may mắn của năm mới.",
                tags = listOf("🎆 Pháo Hoa", "🍲 Mâm Cơm Tất Niên", "🕯️ Trầm Hương", "🏮 Đèn Lồng"),
                isMajorFestival = true
            )
        }

        // 3. Giỗ Tổ Hùng Vương (10/3 ÂL)
        if (lunarDate.month == 3 && lunarDate.day == 10) {
            return HolidayArtBanner(
                title = "🏛️ QUỐC LỄ GIỖ TỔ HÙNG VƯƠNG (10/3 ÂL)",
                subtitle = "Uống Nước Nhớ Nguồn • Tưởng Nhớ Tiền Nhân",
                emojiBig = "👑",
                description = "“Dù ai đi ngược về xuôi / Nhớ ngày Giỗ Tổ mùng mười tháng ba”. Tri ân công lao khai sơn phá thạch dựng cõi cơ đồ non sông Việt Nam.",
                tags = listOf("🏛️ Đền Hùng", "🥁 Trống Đồng", "🚩 Cờ Hội", "🦅 Chim Lạc"),
                isMajorFestival = true
            )
        }

        // 4. Tết Trung Thu (15/8 ÂL)
        if (lunarDate.month == 8 && lunarDate.day in 14..15) {
            return HolidayArtBanner(
                title = "🏮 TẾT TRUNG THU • ĐOÀN VIÊN VIÊN MÃN",
                subtitle = "Đêm Rằm Tháng Tám Sáng Tỏ Muôn Trùng",
                emojiBig = "🥮",
                description = "Đèn lồng ông sao rực rỡ, tiếng trống múa lân rộn rã đầu thôn ngõ xóm, cùng gia đình thưởng thức miếng bánh nướng bánh dẻo ngắm trăng tròn.",
                tags = listOf("🏮 Đèn Ông Sao", "🥮 Bánh Dẻo Nướng", "🌕 Trăng Rằm", "🎭 Đầu Lân"),
                isMajorFestival = true
            )
        }

        // 5. Vu Lan Báo Hiếu (15/7 ÂL)
        if (lunarDate.month == 7 && lunarDate.day in 14..15) {
            return HolidayArtBanner(
                title = "🌹 ĐẠI LỄ VU LAN BÁO HIẾU (15/7 ÂL)",
                subtitle = "Mùa Hiếu Hạnh • Tri Ân Ơn Đức Sinh Thành",
                emojiBig = "🪷",
                description = "Bông hồng cài áo tưởng nhớ công cha nghĩa mẹ, thắp nén tâm hương thanh tịnh dâng lên đấng sinh thành, cầu mong cha mẹ trường thọ an vui.",
                tags = listOf("🌹 Bông Hồng Cài Áo", "🪷 Sen Hồng", "🕯️ Đèn Hoa Đăng", "🙏 Cầu Phúc"),
                isMajorFestival = true
            )
        }

        // 6. Tết Đoan Ngọ (5/5 ÂL)
        if (lunarDate.month == 5 && lunarDate.day == 5) {
            return HolidayArtBanner(
                title = "🌾 TẾT ĐOAN NGỌ • GIẾT SÂU BỌ (5/5 ÂL)",
                subtitle = "Tiết Khí Phương Nam • Hoa Trái Trĩu Cành",
                emojiBig = "🍇",
                description = "Sáng sớm thưởng thức cơm rượu nếp thơm lừng, quả mận hậu giòn ngọt, bánh tro mật mía thanh mát để phòng trừ bệnh tật và cầu mùa màng bội thu.",
                tags = listOf("🍚 Cơm Rượu Nếp", "🍇 Mận Hậu", "🍃 Bánh Tro", "🌿 Lá Thơm"),
                isMajorFestival = true
            )
        }

        // 7. Quốc Khánh 2/9 & Ngày Giải Phóng 30/4
        if ((solarDate.month == 9 && solarDate.day == 2) || (solarDate.month == 4 && solarDate.day == 30)) {
            val holidayName = if (solarDate.month == 9) "TẾT ĐỘC LẬP • QUỐC KHÁNH 2/9" else "NGÀY GIẢI PHÓNG MIỀN NAM 30/4"
            return HolidayArtBanner(
                title = "🇻🇳 $holidayName",
                subtitle = "Độc Lập • Tự Do • Hạnh Phúc",
                emojiBig = "⭐",
                description = "Cờ đỏ sao vàng tung bay rực rỡ khắp non sông gấm vóc đất nước. Tự hào truyền thống anh hùng và hòa bình bền vững của dân tộc Việt Nam.",
                tags = listOf("🇻🇳 Cờ Đỏ Sao Vàng", "⭐ Quảng Trường", "🕊️ Hòa Bình", "🎉 Tự Hào"),
                isMajorFestival = true
            )
        }

        // 8. Ngày Sóc (Mùng 1 ÂL)
        if (lunarDate.day == 1) {
            return HolidayArtBanner(
                title = "🪷 NGÀY SÓC (MÙNG MỘT ĐẦU THÁNG)",
                subtitle = "Khởi Đầu Tháng Mới • Vạn Sự Hanh Thông",
                emojiBig = "🪷",
                description = "Hương trầm thoang thoảng, hoa tươi quả ngọt dâng cúng gia tiên. Khởi sự tháng mới với tâm hồn thanh tịnh, cầu mong mọi sự bình an suôn sẻ.",
                tags = listOf("🪷 Hoa Tươi", "🍎 Mâm Quả Ngọt", "🕯️ Trầm Hương", "🙏 Tâm Thanh Tịnh")
            )
        }

        // 9. Ngày Vọng (Ngày Rằm 15 ÂL)
        if (lunarDate.day == 15) {
            return HolidayArtBanner(
                title = "🌕 NGÀY VỌNG (RẰM TRÒN VIÊN MÃN)",
                subtitle = "Đêm Rằm Sáng Tỏ • Vượng Khí Gia Đạo",
                emojiBig = "🌕",
                description = "Trăng rằm sáng tỏ muôn phương, ánh sáng chiếu rọi cát khí hanh thông. Thích hợp lễ tạ ơn thần linh gia tiên, cầu chúc gia đình hòa thuận ấm no.",
                tags = listOf("🌕 Trăng Rằm", "🪷 Sen Vàng", "🕯️ Ngọn Đèn Dầu", "✨ Vượng Tài Khí")
            )
        }

        // 10. Ngày Thường Dân Gian
        return HolidayArtBanner(
            title = "🌿 LỊCH ÂM THUẦN VIỆT • NGÀY MỚI CÁT LÀNH",
            subtitle = "Năm ${lunarDate.canChiYear} • Tiết ${lunarDate.solarTerm}",
            emojiBig = "🌾",
            description = "Thuận theo nhịp điệu đất trời và tinh hoa lịch học ngàn năm dân tộc. Giữ tâm thế an nhiên, vững vàng đón nhận một ngày tràn ngập may mắn.",
            tags = listOf("🌾 Nắng Mai", "🎋 Lũy Tre Xanh", "🏮 Bình An", "🕊️ May Mắn")
        )
    }
}
