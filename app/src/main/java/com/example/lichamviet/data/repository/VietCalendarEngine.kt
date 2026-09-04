package com.example.lichamviet.data.repository

import com.example.lichamviet.data.model.LunarDate
import com.example.lichamviet.data.model.SolarDate
import com.example.lichamviet.data.model.ZodiacHour
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.sin

/**
 * Thuật toán tính Âm lịch Việt Nam chuẩn múi giờ GMT+7 (Hồ Ngọc Đức).
 * Tính toán thiên văn dựa trên kinh độ mặt trời và tuần trăng mới.
 */
object VietCalendarEngine {

    const val TIME_ZONE = 7.0

    private val CAN = listOf("Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý")
    private val CHI = listOf("Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi")

    private val TIET_KHI = listOf(
        "Xuân Phân", "Thanh Minh", "Cốc Vũ", "Lập Hạ",
        "Tiểu Mãn", "Mang Chủng", "Hạ Chí", "Tiểu Thử",
        "Đại Thử", "Lập Thu", "Xử Thử", "Bạch Lộ",
        "Thu Phân", "Hàn Lộ", "Sương Giáng", "Lập Đông",
        "Tiểu Tuyết", "Đại Tuyết", "Đông Chí", "Tiểu Hàn",
        "Đại Hàn", "Lập Xuân", "Vũ Thủy", "Kinh Trập"
    )

    private val GIO_NAMES = listOf(
        "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ",
        "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi"
    )

    private val GIO_RANGES = listOf(
        "23:00 - 01:00", "01:00 - 03:00", "03:00 - 05:00",
        "05:00 - 07:00", "07:00 - 09:00", "09:00 - 11:00",
        "11:00 - 13:00", "13:00 - 15:00", "15:00 - 17:00",
        "17:00 - 19:00", "19:00 - 21:00", "21:00 - 23:00"
    )

    private val HOANG_DAO_STARS = listOf(
        "Thanh Long", "Minh Đường", "Thiên Hình", "Chu Tước",
        "Kim Quỹ", "Thiên Đức", "Bạch Hổ", "Ngọc Đường",
        "Thiên Lao", "Huyền Vũ", "Tư Mệnh", "Câu Trận"
    )

    private val IS_HOANG_DAO_STAR = listOf(
        true, true, false, false,
        true, true, false, true,
        false, false, true, false
    )

    // Chuyển đổi ngày dương sang Julian Day
    fun jdFromDate(dd: Int, mm: Int, yy: Int): Int {
        val a = (14 - mm) / 12
        val y = yy + 4800 - a
        val m = mm + 12 * a - 3
        var jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045
        if (jd < 2299161) {
            jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - 32083
        }
        return jd
    }

    // Chuyển đổi Julian Day sang ngày dương (day, month, year)
    fun jdToDate(jd: Int): Triple<Int, Int, Int> {
        val a: Int = if (jd > 2299160) {
            val alpha = floor((jd - 1867216.25) / 36524.25).toInt()
            jd + 1 + alpha - (alpha / 4)
        } else {
            jd
        }
        val b = a + 1524
        val c = floor((b - 122.1) / 365.25).toInt()
        val d = floor(365.25 * c).toInt()
        val e = floor((b - d) / 30.6001).toInt()
        val day = b - d - floor(30.6001 * e).toInt()
        val month = if (e < 14) e - 1 else e - 13
        val year = if (month > 2) c - 4716 else c - 4715
        return Triple(day, month, year)
    }

    // Tính ngày Sóc (New Moon) thứ k
    fun getNewMoonDay(k: Int, timeZone: Double = TIME_ZONE): Int {
        val t = k / 1236.85
        val t2 = t * t
        val t3 = t2 * t
        val dr = PI / 180.0
        var jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * t2 - 0.000000155 * t3
        jd1 += 0.00033 * sin((166.56 + 132.87 * t - 0.009173 * t2) * dr)
        val m = 359.2242 + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3
        val mpr = 306.0253 + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3
        val f = 21.2964 + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3
        var c1 = (0.1734 - 0.000393 * t) * sin(m * dr) + 0.0021 * sin(2 * m * dr)
        c1 -= 0.4068 * sin(mpr * dr) + 0.0161 * sin(2 * mpr * dr)
        c1 -= 0.0004 * sin(3 * mpr * dr)
        c1 += 0.0104 * sin(2 * f * dr) - 0.0051 * sin((m + mpr) * dr)
        c1 -= 0.0074 * sin((m - mpr) * dr) + 0.0004 * sin((2 * f + m) * dr)
        c1 -= 0.0004 * sin((2 * f - m) * dr) - 0.0006 * sin((2 * f + mpr) * dr)
        c1 += 0.0010 * sin((2 * f - mpr) * dr) + 0.0005 * sin((m + 2 * mpr) * dr)
        val jd = jd1 + c1
        return floor(jd + 0.5 + timeZone / 24.0).toInt()
    }

    // Tính kinh độ Mặt Trời (Sun Longitude) tại một ngày Julian
    fun getSunLongitude(jdn: Int, timeZone: Double = TIME_ZONE): Double {
        val t = (jdn - 0.5 - timeZone / 24.0 - 2451545.0) / 36525.0
        val t2 = t * t
        val dr = PI / 180.0
        val l0 = 280.46645 + 36000.76983 * t + 0.0003032 * t2
        val m = 357.52910 + 35999.05030 * t - 0.0001559 * t2 - 0.00000048 * t * t2
        val c = (1.914600 - 0.004817 * t - 0.000014 * t2) * sin(m * dr) +
                (0.019993 - 0.000101 * t) * sin(2 * m * dr) +
                0.000290 * sin(3 * m * dr)
        var theta = (l0 + c) * dr
        theta -= 2 * PI * floor(theta / (2 * PI))
        return theta
    }

    // Tiết khí tại một ngày dương
    fun getSolarTerm(dd: Int, mm: Int, yy: Int): String {
        val jd = jdFromDate(dd, mm, yy)
        val sunLong = getSunLongitude(jd)
        val index = floor(sunLong / (PI / 12)).toInt() % 24
        return TIET_KHI[(index + 24) % 24]
    }

    // Tìm tháng 11 âm lịch (Đông Chí) của năm dương lịch
    private fun getLunarMonth11(yy: Int, timeZone: Double = TIME_ZONE): Int {
        var off = jdFromDate(31, 12, yy) - 2415021
        var k = floor(off / 29.530588853).toInt()
        var nm = getNewMoonDay(k, timeZone)
        val sunLong = getSunLongitude(nm, timeZone)
        // Trung khí thứ 9 tương ứng Đông Chí (270 độ = 3*PI/2)
        val sunLongTerm = floor(sunLong / (PI / 6)).toInt()
        if (sunLongTerm >= 9) {
            nm = getNewMoonDay(k - 1, timeZone)
        }
        return nm
    }

    // Tìm tháng nhuận trong năm âm lịch (nếu có 13 tháng)
    private fun getLeapMonthOffset(a11: Int, timeZone: Double = TIME_ZONE): Int {
        var k = floor((a11 - 2415021.076998695) / 29.530588853 + 0.5).toInt()
        var last = 0
        var i = 1
        var arc = floor(getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone) / (PI / 6)).toInt()
        do {
            last = arc
            i++
            arc = floor(getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone) / (PI / 6)).toInt()
        } while (arc != last && i < 14)
        return i - 1
    }

    /**
     * Chuyển đổi Ngày Dương sang Ngày Âm
     */
    fun convertSolar2Lunar(dd: Int, mm: Int, yy: Int): LunarDate {
        val jd = jdFromDate(dd, mm, yy)
        val k = floor((jd - 2415021.076998695) / 29.530588853).toInt()
        var monthStart = getNewMoonDay(k + 1, TIME_ZONE)
        var nmIndex = k + 1
        if (monthStart > jd) {
            monthStart = getNewMoonDay(k, TIME_ZONE)
            nmIndex = k
        }

        var a11 = getLunarMonth11(yy, TIME_ZONE)
        var b11 = a11
        var lunarYear: Int
        if (a11 >= monthStart) {
            lunarYear = yy
            a11 = getLunarMonth11(yy - 1, TIME_ZONE)
        } else {
            lunarYear = yy + 1
            b11 = getLunarMonth11(yy + 1, TIME_ZONE)
        }

        val lunarDay = jd - monthStart + 1
        val diff = floor((monthStart - a11) / 29.0).toInt()
        var isLeap = false
        var lunarMonth = diff + 11
        if (b11 - a11 > 365) {
            val leapMonthDiff = getLeapMonthOffset(a11, TIME_ZONE)
            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10
                if (diff == leapMonthDiff) {
                    isLeap = true
                }
            }
        }
        if (lunarMonth > 12) {
            lunarMonth -= 12
        }
        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1
        }

        // Can Chi của Năm (tính theo năm âm lịch)
        val canYear = CAN[(lunarYear + 6) % 10]
        val chiYear = CHI[(lunarYear + 8) % 12]

        // Can Chi của Tháng
        val canMonth = CAN[(lunarYear * 2 + lunarMonth + 1) % 10]
        val chiMonth = CHI[(lunarMonth + 1) % 12]

        // Can Chi của Ngày (tính theo Julian Day)
        val canDay = CAN[(jd + 9) % 10]
        val chiDay = CHI[(jd + 1) % 12]

        // Tiết khí
        val solarTerm = getSolarTerm(dd, mm, yy)

        // Giờ Hoàng Đạo
        val chiDayIndex = (jd + 1) % 12
        val auspiciousHours = calculateZodiacHours(chiDayIndex)

        // Ngày Hoàng Đạo / Hắc Đạo
        val isAuspiciousDay = isDayAuspicious(lunarMonth, chiDayIndex)
        val dayRating = if (isAuspiciousDay) "Hoàng Đạo" else "Hắc Đạo"

        // Hướng xuất hành
        val canDayIndex = (jd + 9) % 10
        val (hyThan, taiThan) = calculateTravelDirections(canDayIndex)

        return LunarDate(
            day = lunarDay,
            month = lunarMonth,
            year = lunarYear,
            isLeapMonth = isLeap,
            canDay = canDay,
            chiDay = chiDay,
            canMonth = canMonth,
            chiMonth = chiMonth,
            canYear = canYear,
            chiYear = chiYear,
            solarTerm = solarTerm,
            isAuspiciousDay = isAuspiciousDay,
            dayRating = dayRating,
            auspiciousHours = auspiciousHours,
            hyThanDirection = hyThan,
            taiThanDirection = taiThan
        )
    }

    /**
     * Chuyển đổi Ngày Âm sang Ngày Dương
     */
    fun convertLunar2Solar(lunarDay: Int, lunarMonth: Int, lunarYear: Int, isLeap: Boolean): SolarDate {
        val a11: Int
        val b11: Int
        if (lunarMonth < 11) {
            a11 = getLunarMonth11(lunarYear - 1, TIME_ZONE)
            b11 = getLunarMonth11(lunarYear, TIME_ZONE)
        } else {
            a11 = getLunarMonth11(lunarYear, TIME_ZONE)
            b11 = getLunarMonth11(lunarYear + 1, TIME_ZONE)
        }

        var k = floor(0.5 + (a11 - 2415021.076998695) / 29.530588853).toInt()
        var off = lunarMonth - 11
        if (off < 0) {
            off += 12
        }
        if (b11 - a11 > 365) {
            val leapOff = getLeapMonthOffset(a11, TIME_ZONE)
            var leapMonth = leapOff - 2
            if (leapMonth < 0) {
                leapMonth += 12
            }
            if (isLeap && lunarMonth != leapMonth) {
                // Không phải tháng nhuận
            } else if (isLeap || off >= leapOff) {
                off += 1
            }
        }
        val monthStart = getNewMoonDay(k + off, TIME_ZONE)
        val jd = monthStart + lunarDay - 1
        val (dd, mm, yy) = jdToDate(jd)
        val dayOfWeek = (jd + 1) % 7 + 1 // 1: Chủ Nhật, 2: Thứ 2...
        return SolarDate(dd, mm, yy, dayOfWeek)
    }

    /**
     * Tính Giờ Hoàng Đạo trong ngày theo Chi của Ngày
     */
    fun calculateZodiacHours(chiDayIndex: Int): List<ZodiacHour> {
        // Vị trí sao bắt đầu cho từng Chi ngày
        // Dần (2), Thân (8) -> Tý là Thanh Long
        // Mão (3), Dậu (9) -> Dần là Thanh Long
        // Thìn (4), Tuất (10) -> Thìn là Thanh Long
        // Tỵ (5), Hợi (11) -> Ngọ là Thanh Long
        // Tý (0), Ngọ (6) -> Thân là Thanh Long
        // Sửu (1), Mùi (7) -> Tuất là Thanh Long
        val startOffset = when (chiDayIndex) {
            2, 8 -> 0   // Dần, Thân: Tý là Thanh Long
            3, 9 -> 10  // Mão, Dậu: Dần là Thanh Long -> Tý là Câu Trận (index 10)
            4, 10 -> 8  // Thìn, Tuất: Thìn là Thanh Long -> Tý là Thiên Lao (index 8)
            5, 11 -> 6  // Tỵ, Hợi: Ngọ là Thanh Long -> Tý là Bạch Hổ (index 6)
            0, 6 -> 4   // Tý, Ngọ: Thân là Thanh Long -> Tý là Kim Quỹ (index 4)
            1, 7 -> 2   // Sửu, Mùi: Tuất là Thanh Long -> Tý là Thiên Hình (index 2)
            else -> 0
        }

        return (0..11).map { hourIndex ->
            val starIndex = (hourIndex + startOffset) % 12
            val isAuspicious = IS_HOANG_DAO_STAR[starIndex]
            val starName = HOANG_DAO_STARS[starIndex]
            ZodiacHour(
                name = GIO_NAMES[hourIndex],
                timeRange = GIO_RANGES[hourIndex],
                starName = starName,
                isAuspicious = isAuspicious
            )
        }
    }

    /**
     * Đánh giá Ngày Hoàng Đạo / Hắc Đạo dựa vào Chi tháng và Chi ngày
     */
    fun isDayAuspicious(lunarMonth: Int, chiDayIndex: Int): Boolean {
        // Chi của tháng: Tháng 1 là Dần (2), Tháng 2 là Mão (3)... Tháng 11 là Tý (0), Tháng 12 là Sửu (1)
        val chiMonth = (lunarMonth + 1) % 12
        // Bảng Hoàng đạo: Thanh Long, Minh Đường, Kim Quỹ, Thiên Đức, Ngọc Đường, Tư Mệnh
        val diff = (chiDayIndex - chiMonth + 12) % 12
        return diff in listOf(0, 1, 4, 5, 7, 10)
    }

    /**
     * Tính hướng xuất hành cát lợi: Hỷ Thần và Tài Thần
     */
    fun calculateTravelDirections(canDayIndex: Int): Pair<String, String> {
        return when (canDayIndex) {
            0 -> Pair("Hướng Đông Bắc", "Hướng Đông Nam") // Giáp
            1 -> Pair("Hướng Tây Bắc", "Hướng Đông Nam")  // Ất
            2 -> Pair("Hướng Tây Nam", "Hướng Chính Đông") // Bính
            3 -> Pair("Hướng Chính Nam", "Hướng Chính Đông") // Đinh
            4 -> Pair("Hướng Đông Nam", "Hướng Chính Bắc") // Mậu
            5 -> Pair("Hướng Đông Bắc", "Hướng Chính Nam") // Kỷ
            6 -> Pair("Hướng Tây Bắc", "Hướng Tây Nam")   // Canh
            7 -> Pair("Hướng Tây Nam", "Hướng Tây Nam")   // Tân
            8 -> Pair("Hướng Chính Nam", "Hướng Chính Tây") // Nhâm
            9 -> Pair("Hướng Đông Nam", "Hướng Chính Tây") // Quý
            else -> Pair("Hướng Đông Nam", "Hướng Chính Nam")
        }
    }
}
