package com.example.lichamviet.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppTheme(
    val id: String,
    val displayName: String,
    val colorHex: Long,
    val isDark: Boolean = false
) {
    MODERN_BLUE("modern_blue", "Xanh Hiện Đại", 0xFF0B57D0),
    JADE_GREEN("jade_green", "Xanh Ngọc Bích", 0xFF006C4C),
    AMBER_WARM("amber_warm", "Vàng Hổ Phách", 0xFF855300),
    DARK_SLEEK("dark_sleek", "Tối Trầm Hiện Đại", 0xFF1D2024, isDark = true)
}

data class VipPlan(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String,
    val discountBadge: String = "",
    val isPopular: Boolean = false,
    val period: String
)

data class UserProfile(
    val name: String,
    val email: String,
    val provider: String,
    val avatarInitials: String
)

data class UserPreferences(
    val isVip: Boolean = false,
    val currentPlan: String = "Miễn phí",
    val theme: AppTheme = AppTheme.MODERN_BLUE,
    val isLargeFont: Boolean = false,
    val notifyMung1Ram: Boolean = true,
    val notifyHolidays: Boolean = true,
    val morningNotifyHour: Int = 7,
    val useDynamicColor: Boolean = false,
    val userProfile: UserProfile? = null
)

object UserPreferencesRepository {

    val VIP_PLANS = listOf(
        VipPlan(
            id = "plan_1_month",
            name = "Gói 1 Tháng (Khởi Sự)",
            price = "19.000 đ",
            originalPrice = "39.000 đ",
            discountBadge = "Tiết kiệm 50%",
            period = "/ tháng"
        ),
        VipPlan(
            id = "plan_1_year",
            name = "Gói 1 Năm (Thịnh Vượng)",
            price = "99.000 đ",
            originalPrice = "228.000 đ",
            discountBadge = "Giảm 55% • Phổ Biến Nhất",
            isPopular = true,
            period = "/ năm (chỉ ~8k/tháng)"
        ),
        VipPlan(
            id = "plan_lifetime",
            name = "Gói Trọn Đời (Vĩnh Cửu Lộc)",
            price = "199.000 đ",
            originalPrice = "999.000 đ",
            discountBadge = "Mua 1 Lần Dùng Mãi Mãi",
            period = "Dùng trọn đời"
        )
    )

    private val _preferences = MutableStateFlow(UserPreferences())
    val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    fun activateVip(plan: VipPlan) {
        _preferences.value = _preferences.value.copy(
            isVip = true,
            currentPlan = plan.name
        )
    }

    fun restorePurchase() {
        _preferences.value = _preferences.value.copy(
            isVip = true,
            currentPlan = "Gói Trọn Đời (Đã khôi phục)"
        )
    }

    fun cancelVip() {
        _preferences.value = _preferences.value.copy(
            isVip = false,
            currentPlan = "Miễn phí"
        )
    }

    fun setTheme(theme: AppTheme) {
        _preferences.value = _preferences.value.copy(theme = theme)
    }

    fun toggleLargeFont(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(isLargeFont = enabled)
    }

    fun setNotifyMung1Ram(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(notifyMung1Ram = enabled)
    }

    fun setNotifyHolidays(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(notifyHolidays = enabled)
    }

    fun setMorningNotifyHour(hour: Int) {
        _preferences.value = _preferences.value.copy(morningNotifyHour = hour)
    }

    fun setUseDynamicColor(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(useDynamicColor = enabled)
    }

    fun setUserProfile(profile: UserProfile?) {
        _preferences.value = _preferences.value.copy(userProfile = profile)
    }

    fun logout() {
        _preferences.value = _preferences.value.copy(userProfile = null)
    }
}
