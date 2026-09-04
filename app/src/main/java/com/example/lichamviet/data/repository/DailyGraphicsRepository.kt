package com.example.lichamviet.data.repository

data class ZodiacAnimalInfo(
    val chi: String,
    val vietnameseAnimal: String,
    val iconEmoji: String,
    val symbolism: String,
    val auspiciousNote: String
)

data class MoonPhaseInfo(
    val phaseName: String,
    val phaseDetail: String,
    val iconEmoji: String,
    val culturalNote: String
)

object DailyGraphicsRepository {

    private val ZODIAC_ANIMALS = mapOf(
        "Tý" to ZodiacAnimalInfo("Tý", "Chuột", "🐭", "Thông minh, cần cù, tích lũy tài lộc", "Cát tinh chiếu mệnh, thích hợp hoạch định kế hoạch và quản lý tiền bạc."),
        "Sửu" to ZodiacAnimalInfo("Sửu", "Trâu", "🐂", "Kiên định, cần mẫn, đất đai ấm no", "Địa chi thổ sinh tài, việc kinh doanh và đất đai thuận lợi bền vững."),
        "Dần" to ZodiacAnimalInfo("Dần", "Hổ", "🐯", "Dũng mãnh, quyền uy, khí phách bứt phá", "Mộc vượng sinh vinh hoa, dám nghĩ dám làm sẽ đón nhận thắng lợi lớn."),
        "Mão" to ZodiacAnimalInfo("Mão", "Mèo", "🐱", "Linh hoạt, may mắn, hòa nhã chiêu tài", "Linh vật Mèo thuần Việt tượng trưng cho sự thanh nhã, gia đạo hòa thuận."),
        "Thìn" to ZodiacAnimalInfo("Thìn", "Rồng", "🐲", "Thăng tiến, uy phong, mưa thuận gió hòa", "Rồng vàng vươn mình, công danh sự nghiệp và thi cử hanh thông đại cát."),
        "Tỵ" to ZodiacAnimalInfo("Tỵ", "Rắn", "🐍", "Khôn ngoan, nhạy bén, sâu sắc biến hóa", "Trí tuệ minh mẫn, giải quyết các khúc mắc và ký kết văn tự chuẩn xác."),
        "Ngọ" to ZodiacAnimalInfo("Ngọ", "Ngựa", "🐴", "Tự do, dũng tiến, mã đáo thành công", "Hỏa khí hưng thịnh, xuất hành hay mở mang buôn bán gặt hái tin vui."),
        "Mùi" to ZodiacAnimalInfo("Mùi", "Dê", "🐐", "Ôn hòa, nhân hậu, tài lộc hanh thông", "Thuần hậu sinh cát khí, kết giao bạn bè hay bàn việc gia đình tốt đẹp."),
        "Thân" to ZodiacAnimalInfo("Thân", "Khỉ", "🐵", "Nhanh trí, lanh lợi, xoay chuyển tài tình", "Biến nguy thành an, nhiều sáng kiến và giải pháp mới mẻ thành công."),
        "Dậu" to ZodiacAnimalInfo("Dậu", "Gà", "🐓", "Cần mẫn, đúng giờ, đón bình minh rạng rỡ", "Kim thanh dẫn đường, gia đình êm ấm, tinh thần lạc quan phấn chấn."),
        "Tuất" to ZodiacAnimalInfo("Tuất", "Chó", "🐶", "Trung thành, bảo hộ, an khang thịnh vượng", "Độ trung tín cao, hợp tác kinh doanh hay làm việc nhóm đạt hiệu quả cao."),
        "Hợi" to ZodiacAnimalInfo("Hợi", "Lợn", "🐷", "Sung túc, an nhàn, phúc lộc trời ban", "Thủy nhu dưỡng sinh, gia đạo dồi dào tài lộc, tâm an vạn sự như ý.")
    )

    fun getZodiacAnimalForDay(canChiDay: String): ZodiacAnimalInfo {
        val parts = canChiDay.trim().split(" ")
        val chi = if (parts.isNotEmpty()) parts.last() else "Tý"
        return ZODIAC_ANIMALS[chi] ?: ZodiacAnimalInfo(
            chi = chi,
            vietnameseAnimal = "Linh Vật",
            iconEmoji = "🌟",
            symbolism = "Cát tường như ý",
            auspiciousNote = "Ngày lành đem lại may mắn và an khang cho muôn nhà."
        )
    }

    fun getMoonPhaseForDay(lunarDay: Int): MoonPhaseInfo {
        return when (lunarDay) {
            1 -> MoonPhaseInfo("Ngày Sóc (Trăng Non)", "Đầu tháng âm lịch, trăng ẩn mình", "🌑", "Khởi đầu chu kỳ mới, tâm trí tĩnh tại, thích hợp lễ bái gia tiên và phóng sinh cầu an.")
            in 2..6 -> MoonPhaseInfo("Trăng Lưỡi Liềm Đầu Tháng", "Trăng mọc phía tây lúc hoàng hôn", "🌒", "Ánh trăng non dần ló rạng, sinh khí đất trời bừng nở, vạn vật phát triển.")
            in 7..8 -> MoonPhaseInfo("Thượng Huyền (Bán Nguyệt)", "Nửa vầng trăng soi tỏ bầu trời", "🌓", "Âm dương cân bằng, năng lượng hài hòa, làm việc gì cũng vững chắc.")
            in 9..13 -> MoonPhaseInfo("Trăng Khuyết Dần Đầy", "Trăng lớn dần hướng tới viên mãn", "🌔", "Khí lực dồi dào, các kế hoạch lớn đang dần bước vào giai đoạn thu hái quả ngọt.")
            in 14..16 -> MoonPhaseInfo("Ngày Vọng (Trăng Rằm Tròn Đầy)", "Trăng tròn vành vạnh, ánh sáng rực rỡ nhất", "🌕", "Đại cát đại vượng, đêm rằm sáng tỏ muôn trùng, thích hợp cúng tạ ơn và đoàn viên.")
            in 17..21 -> MoonPhaseInfo("Trăng Khuyết Sau Rằm", "Trăng mọc muộn dần sau hoàng hôn", "🌖", "Ánh sáng lắng đọng, lòng người thư thái, thu hoạch tài lộc và tích trữ phúc đức.")
            in 22..23 -> MoonPhaseInfo("Hạ Huyền (Bán Nguyệt Cuối Tháng)", "Trăng khuyết một nửa mọc lúc nửa đêm", "🌗", "Khoảng lặng điều chỉnh, thích hợp tổng kết công việc và vun đắp các mối quan hệ.")
            in 24..28 -> MoonPhaseInfo("Trăng Tàn Cuối Tháng", "Lưỡi liềm mảnh mai lúc rạng đông", "🌘", "Chu kỳ sắp hoàn tất, xua tan muộn phiền, đón chờ một tháng mới tươi sáng.")
            else -> MoonPhaseInfo("Đêm Hối (Trăng Lặn)", "Ngày cuối tháng âm lịch, trời tối mịt", "🌑", "Kết thúc tháng cũ trọn vẹn, dọn dẹp nhà cửa, gột rửa âu lo đón chào sóc mới.")
        }
    }
}
