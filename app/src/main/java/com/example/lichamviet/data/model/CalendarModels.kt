package com.example.lichamviet.data.model

data class SolarDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val dayOfWeek: Int // 1 = Chủ Nhật, 2 = Thứ Hai, ..., 7 = Thứ Bảy
) {
    val isSunday: Boolean get() = dayOfWeek == 1

    val dayOfWeekName: String
        get() = when (dayOfWeek) {
            1 -> "Chủ Nhật"
            2 -> "Thứ Hai"
            3 -> "Thứ Ba"
            4 -> "Thứ Tư"
            5 -> "Thứ Năm"
            6 -> "Thứ Sáu"
            7 -> "Thứ Bảy"
            else -> ""
        }

    val dayOfWeekShort: String
        get() = when (dayOfWeek) {
            1 -> "CN"
            2 -> "T2"
            3 -> "T3"
            4 -> "T4"
            5 -> "T5"
            6 -> "T6"
            7 -> "T7"
            else -> ""
        }
}

data class LunarDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val isLeapMonth: Boolean = false,
    val canDay: String = "",
    val chiDay: String = "",
    val canMonth: String = "",
    val chiMonth: String = "",
    val canYear: String = "",
    val chiYear: String = "",
    val solarTerm: String = "", // Tiết khí
    val isAuspiciousDay: Boolean = true, // Ngày hoàng đạo
    val dayRating: String = "Hoàng Đạo", // Hoàng Đạo / Hắc Đạo
    val auspiciousHours: List<ZodiacHour> = emptyList(),
    val hyThanDirection: String = "", // Hướng Hỷ Thần
    val taiThanDirection: String = ""  // Hướng Tài Thần
) {
    val canChiDay: String get() = "$canDay $chiDay"
    val canChiMonth: String get() = "$canMonth $chiMonth"
    val canChiYear: String get() = "$canYear $chiYear"
    val monthName: String
        get() = when (month) {
            1 -> if (isLeapMonth) "Tháng Giêng (Nhuận)" else "Tháng Giêng"
            11 -> if (isLeapMonth) "Tháng Một (Nhuận)" else "Tháng Một"
            12 -> if (isLeapMonth) "Tháng Chạp (Nhuận)" else "Tháng Chạp"
            else -> if (isLeapMonth) "Tháng $month (Nhuận)" else "Tháng $month"
        }
}

data class ZodiacHour(
    val name: String,        // Tý, Sửu, Dần...
    val timeRange: String,   // 23:00 - 01:00
    val starName: String,    // Thanh Long, Minh Đường...
    val isAuspicious: Boolean // true = Hoàng Đạo, false = Hắc Đạo
)

enum class HolidayType {
    LUNAR_TRADITIONAL, // Lễ, Tết âm lịch truyền thống
    LUNAR_BUDDHIST,    // Lễ Phật giáo (Rằm, Phật đản, Vu Lan...)
    SOLAR_NATIONAL,    // Ngày lễ quốc gia, nghỉ lễ (30/4, 2/9, 1/1...)
    SOLAR_COMMEMORATIVE // Ngày kỷ niệm Việt Nam (20/11, 27/2, 8/3...)
}

data class Holiday(
    val id: String,
    val name: String,
    val isLunar: Boolean,
    val day: Int,
    val month: Int,
    val type: HolidayType,
    val isNationalDayOff: Boolean = false, // Nghỉ lễ chính thức
    val subtitle: String = "",
    val description: String = "",
    val customs: List<String> = emptyList() // Phong tục tập quán đặc trưng
)

data class VanKhan(
    val id: String,
    val title: String,
    val category: String, // "Tết & Giao Thừa", "Mùng 1 & Rằm", "Cúng Gia Tiên", "Đền Chùa - Thần Linh"
    val occasion: String,
    val preparation: String, // Lễ vật cần sắm
    val content: String      // Toàn văn bài cúng
)
