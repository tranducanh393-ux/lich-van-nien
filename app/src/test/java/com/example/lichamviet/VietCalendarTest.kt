package com.example.lichamviet

import com.example.lichamviet.data.model.SolarDate
import com.example.lichamviet.data.repository.HolidayRepository
import com.example.lichamviet.data.repository.VietCalendarEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VietCalendarTest {

    @Test
    fun testTetNguyenDan2024() {
        // Mùng 1 Tết Giáp Thìn 2024 rơi vào ngày 10/02/2024
        val lunar = VietCalendarEngine.convertSolar2Lunar(10, 2, 2024)
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2024, lunar.year)
        assertEquals("Giáp", lunar.canYear)
        assertEquals("Thìn", lunar.chiYear)
    }

    @Test
    fun testTetNguyenDan2025() {
        // Mùng 1 Tết Ất Tỵ 2025 rơi vào ngày 29/01/2025
        val lunar = VietCalendarEngine.convertSolar2Lunar(29, 1, 2025)
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2025, lunar.year)
        assertEquals("Ất", lunar.canYear)
        assertEquals("Tỵ", lunar.chiYear)
    }

    @Test
    fun testTetNguyenDan2026() {
        // Mùng 1 Tết Bính Ngọ 2026 rơi vào ngày 17/02/2026
        val lunar = VietCalendarEngine.convertSolar2Lunar(17, 2, 2026)
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2026, lunar.year)
        assertEquals("Bính", lunar.canYear)
        assertEquals("Ngọ", lunar.chiYear)
    }

    @Test
    fun testGioToHungVuong2024() {
        // 10/3 Giáp Thìn 2024 rơi vào ngày 18/04/2024
        val solar = VietCalendarEngine.convertLunar2Solar(10, 3, 2024, false)
        assertEquals(18, solar.day)
        assertEquals(4, solar.month)
        assertEquals(2024, solar.year)

        val lunar = VietCalendarEngine.convertSolar2Lunar(18, 4, 2024)
        assertEquals(10, lunar.day)
        assertEquals(3, lunar.month)
    }

    @Test
    fun testTrungThu2024() {
        // 15/8 Giáp Thìn 2024 rơi vào ngày 17/09/2024
        val solar = VietCalendarEngine.convertLunar2Solar(15, 8, 2024, false)
        assertEquals(17, solar.day)
        assertEquals(9, solar.month)
        assertEquals(2024, solar.year)
    }

    @Test
    fun testRoundTripConversion() {
        // Chuyển đổi hai chiều Solar -> Lunar -> Solar phải khớp nhau
        val testDays = listOf(
            Triple(1, 1, 2026),
            Triple(17, 2, 2026),
            Triple(30, 4, 2026),
            Triple(2, 9, 2026),
            Triple(31, 12, 2026)
        )
        for ((d, m, y) in testDays) {
            val lunar = VietCalendarEngine.convertSolar2Lunar(d, m, y)
            val roundTripSolar = VietCalendarEngine.convertLunar2Solar(lunar.day, lunar.month, lunar.year, lunar.isLeapMonth)
            assertEquals("Ngày phải khớp cho $d/$m/$y", d, roundTripSolar.day)
            assertEquals("Tháng phải khớp cho $d/$m/$y", m, roundTripSolar.month)
            assertEquals("Năm phải khớp cho $d/$m/$y", y, roundTripSolar.year)
        }
    }

    @Test
    fun testHolidaysDetection() {
        // Kiểm tra phát hiện ngày lễ 30/4
        val solar304 = SolarDate(30, 4, 2026, 5)
        val lunar304 = VietCalendarEngine.convertSolar2Lunar(30, 4, 2026)
        val holidays304 = HolidayRepository.getHolidaysForDate(solar304, lunar304)
        assertTrue(holidays304.any { it.name.contains("Thống nhất Đất nước") })

        // Kiểm tra phát hiện Tết Mùng 1
        val solarTet = SolarDate(17, 2, 2026, 3)
        val lunarTet = VietCalendarEngine.convertSolar2Lunar(17, 2, 2026)
        val holidaysTet = HolidayRepository.getHolidaysForDate(solarTet, lunarTet)
        assertTrue(holidaysTet.any { it.name.contains("Tết Nguyên Đán") })
    }

    @Test
    fun testAuspiciousHours() {
        // Đảm bảo mỗi ngày luôn có đúng 6 giờ hoàng đạo và 6 giờ hắc đạo
        val hours = VietCalendarEngine.calculateZodiacHours(0)
        assertEquals(12, hours.size)
        assertEquals(6, hours.count { it.isAuspicious })
        assertEquals(6, hours.count { !it.isAuspicious })
    }
}
