package com.example.lichamviet.data.repository

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

private const val TAG = "CalendarSyncHelper"

/**
 * Tiện ích kết nối với dịch vụ Lịch trên máy và Google Calendar.
 * Hỗ trợ tạo sự kiện âm lịch, sóc vọng và ngày lễ trực tiếp vào ứng dụng Lịch Google.
 */
object CalendarSyncHelper {

    /**
     * Mở ứng dụng Google Calendar hoặc Lịch mặc định của thiết bị
     */
    fun openCalendarApp(context: Context) {
        try {
            // Thử mở ứng dụng Google Calendar trực tiếp
            val googleCalIntent = context.packageManager.getLaunchIntentForPackage("com.google.android.calendar")
            if (googleCalIntent != null) {
                context.startActivity(googleCalIntent)
                return
            }

            // Fallback: Mở trình xem lịch mặc định của hệ thống
            val builder = CalendarContract.CONTENT_URI.buildUpon()
            builder.appendPath("time")
            builder.appendPath(System.currentTimeMillis().toString())
            val intent = Intent(Intent.ACTION_VIEW, builder.build()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Không thể mở ứng dụng lịch: ${e.message}")
            Toast.makeText(context, "Không tìm thấy ứng dụng Lịch trên máy", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Mở màn hình tạo sự kiện mới trên Google Calendar / Lịch máy với thông tin âm lịch điền sẵn
     */
    fun addEventToCalendar(
        context: Context,
        title: String,
        description: String,
        solarDate: LocalDate
    ) {
        try {
            val startMillis = solarDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endMillis = startMillis + 24 * 60 * 60 * 1000

            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PUBLIC)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi thêm sự kiện lịch: ${e.message}", e)
            Toast.makeText(context, "Lỗi kết nối Lịch: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Thêm ngày Sóc Vọng (Mùng 1 hoặc Rằm) tiếp theo vào Google Calendar
     */
    fun syncNextSocVongToCalendar(context: Context) {
        try {
            val today = LocalDate.now()
            // Tìm ngày Mùng 1 hoặc Ngày Rằm tiếp theo trong vòng 35 ngày tới
            for (i in 0..35) {
                val targetDate = today.plusDays(i.toLong())
                val lunar = VietCalendarEngine.convertSolar2Lunar(
                    targetDate.dayOfMonth,
                    targetDate.monthValue,
                    targetDate.year
                )
                if (lunar.day == 1) {
                    addEventToCalendar(
                        context = context,
                        title = "Mùng 1 Âm Lịch (${lunar.monthName})",
                        description = "Ngày Sóc đầu tháng âm lịch. Ngày ${lunar.canChiDay}, Năm ${lunar.canChiYear}. Tiết ${lunar.solarTerm}. Tự động đồng bộ bởi Lịch Âm Việt (Trần Đức Anh).",
                        solarDate = targetDate
                    )
                    return
                } else if (lunar.day == 15) {
                    addEventToCalendar(
                        context = context,
                        title = "Rằm Tháng ${lunar.month} Âm Lịch (Vọng)",
                        description = "Ngày Rằm trăng tròn tháng âm lịch. Ngày ${lunar.canChiDay}, Năm ${lunar.canChiYear}. Tiết ${lunar.solarTerm}. Tự động đồng bộ bởi Lịch Âm Việt (Trần Đức Anh).",
                        solarDate = targetDate
                    )
                    return
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi syncNextSocVongToCalendar: ${e.message}")
        }
    }

    /**
     * Chia sẻ thông tin ngày Âm Lịch hôm nay qua các ứng dụng khác trên máy
     */
    fun shareDayInfo(context: Context, date: LocalDate) {
        try {
            val lunar = VietCalendarEngine.convertSolar2Lunar(date.dayOfMonth, date.monthValue, date.year)
            val shareText = """
                📅 LỊCH ÂM VIỆT NAM
                Dương lịch: Ngày ${date.dayOfMonth}/${date.monthValue}/${date.year}
                Âm lịch: Ngày ${lunar.day} ${lunar.monthName} (Năm ${lunar.canChiYear})
                Can Chi: Ngày ${lunar.canChiDay}, Tháng ${lunar.canChiMonth}
                Đánh giá: ${lunar.dayRating} • Tiết khí: ${lunar.solarTerm}
                Hướng xuất hành: Hỷ Thần (${lunar.hyThanDirection}), Tài Thần (${lunar.taiThanDirection})
                
                📱 Ứng dụng Lịch Vạn Niên thuần Việt - Phát triển bởi Trần Đức Anh
            """.trimIndent()

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Chia sẻ ngày Lịch Âm").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Không thể chia sẻ: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
