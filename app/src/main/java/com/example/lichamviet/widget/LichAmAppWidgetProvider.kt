package com.example.lichamviet.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.lichamviet.MainActivity
import com.example.lichamviet.R
import com.example.lichamviet.data.repository.VietCalendarEngine
import java.time.LocalDate

private const val TAG = "LichAmWidget"

/**
 * Tiện ích Widget màn hình chính xem nhanh Lịch Âm & Dương thuần Việt.
 * Hỗ trợ tự động cập nhật và xử lý an toàn chống crash.
 */
class LichAmAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            try {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi khi update widget id $appWidgetId: ${e.message}", e)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        try {
            when (intent.action) {
                Intent.ACTION_TIME_TICK,
                Intent.ACTION_TIME_CHANGED,
                Intent.ACTION_TIMEZONE_CHANGED,
                Intent.ACTION_DATE_CHANGED,
                Intent.ACTION_BOOT_COMPLETED,
                AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                    refreshAllWidgets(context)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi onReceive widget: ${e.message}", e)
        }
    }

    companion object {
        /**
         * Gọi để ép cập nhật lại toàn bộ widget đang ghim trên màn hình chính
         */
        fun refreshAllWidgets(context: Context) {
            try {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val thisWidget = ComponentName(context, LichAmAppWidgetProvider::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
                if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                    for (appWidgetId in appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi khi refreshAllWidgets: ${e.message}", e)
            }
        }

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            try {
                val now = LocalDate.now()
                val day = now.dayOfMonth
                val month = now.monthValue
                val year = now.year

                val dayOfWeekNames = listOf(
                    "Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"
                )
                val dayOfWeek = dayOfWeekNames[now.dayOfWeek.value % 7]

                val lunar = VietCalendarEngine.convertSolar2Lunar(day, month, year)

                val views = RemoteViews(context.packageName, R.layout.widget_lich_am)

                // 1. Cột Dương Lịch
                views.setTextViewText(R.id.tv_widget_day_of_week, dayOfWeek.uppercase())
                views.setTextViewText(R.id.tv_widget_solar_day, "%02d".format(day))
                views.setTextViewText(R.id.tv_widget_solar_month_year, "Tháng %02d, %d".format(month, year))

                // 2. Cột Âm Lịch & Can Chi
                views.setTextViewText(R.id.tv_widget_lunar_day_month, "Ngày ${lunar.day} ${lunar.monthName}")
                views.setTextViewText(R.id.tv_widget_can_chi_day, "Ngày ${lunar.canChiDay}")
                views.setTextViewText(R.id.tv_widget_can_chi_year, "Năm ${lunar.canChiYear}")
                val termDisplay = if (lunar.solarTerm.isNotBlank()) " • Tiết ${lunar.solarTerm}" else ""
                views.setTextViewText(R.id.tv_widget_rating_term, "★ ${lunar.dayRating}$termDisplay")

                // 3. Sự kiện bấm vào Widget -> Mở ứng dụng ngay
                val openAppIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    openAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi updateAppWidget id $appWidgetId: ${e.message}", e)
            }
        }
    }
}
