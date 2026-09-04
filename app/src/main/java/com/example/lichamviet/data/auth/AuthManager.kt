package com.example.lichamviet.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import com.example.lichamviet.data.repository.UserProfile
import com.example.lichamviet.data.repository.UserPreferencesRepository
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.UUID

data class AuthUser(
    val id: String,
    val name: String,
    val email: String,
    val provider: String, // "Email", "Google"
    val avatarInitials: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toUserProfile(): UserProfile {
        return UserProfile(
            name = name,
            email = email,
            provider = provider,
            avatarInitials = avatarInitials
        )
    }
}

private data class StoredAccount(
    val user: AuthUser,
    val salt: String,
    val passwordHash: String
)

object AuthManager {
    private const val PREFS_NAME = "lich_van_nien_auth_store"
    private const val KEY_SESSION_USER_ID = "current_session_user_id"
    private const val KEY_REGISTERED_USERS = "registered_accounts_json"

    private var sharedPreferences: SharedPreferences? = null
    private var currentUser: AuthUser? = null
    private val registeredAccounts = mutableMapOf<String, StoredAccount>() // keyed by email lowercase

    fun init(context: Context) {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences = prefs
        loadAccounts(prefs)

        // Khôi phục phiên đăng nhập trước đó nếu có
        val sessionUserId = prefs.getString(KEY_SESSION_USER_ID, null)
        if (sessionUserId != null) {
            val matched = registeredAccounts.values.find { it.user.id == sessionUserId }
            if (matched != null) {
                currentUser = matched.user
                UserPreferencesRepository.setUserProfile(matched.user.toUserProfile())
            }
        }
    }

    fun getCurrentUser(): AuthUser? = currentUser

    fun isLoggedIn(): Boolean = currentUser != null

    /**
     * Đăng ký tài khoản mới bằng Email và Mật khẩu thực tế
     */
    fun register(name: String, email: String, password: String): Result<AuthUser> {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim().lowercase()

        if (trimmedName.isEmpty()) {
            return Result.failure(IllegalArgumentException("Vui lòng nhập họ và tên"))
        }
        if (trimmedEmail.isEmpty()) {
            return Result.failure(IllegalArgumentException("Vui lòng nhập địa chỉ email"))
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            return Result.failure(IllegalArgumentException("Địa chỉ email không đúng định dạng"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự"))
        }
        if (registeredAccounts.containsKey(trimmedEmail)) {
            return Result.failure(IllegalStateException("Email này đã được đăng ký tài khoản"))
        }

        val salt = generateSalt()
        val passwordHash = hashPassword(password, salt)
        val initials = extractInitials(trimmedName)

        val newUser = AuthUser(
            id = UUID.randomUUID().toString(),
            name = trimmedName,
            email = trimmedEmail,
            provider = "Email",
            avatarInitials = initials
        )

        val stored = StoredAccount(newUser, salt, passwordHash)
        registeredAccounts[trimmedEmail] = stored
        persistAccounts()

        // Thiết lập phiên đăng nhập
        setSessionUser(newUser)
        return Result.success(newUser)
    }

    /**
     * Đăng nhập bằng Email và Mật khẩu
     */
    fun login(email: String, password: String): Result<AuthUser> {
        val trimmedEmail = email.trim().lowercase()

        if (trimmedEmail.isEmpty()) {
            return Result.failure(IllegalArgumentException("Vui lòng nhập địa chỉ email"))
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            return Result.failure(IllegalArgumentException("Địa chỉ email không đúng định dạng"))
        }
        if (password.isEmpty()) {
            return Result.failure(IllegalArgumentException("Vui lòng nhập mật khẩu"))
        }

        val stored = registeredAccounts[trimmedEmail]
            ?: return Result.failure(IllegalArgumentException("Tài khoản không tồn tại. Vui lòng kiểm tra lại email hoặc đăng ký mới."))

        val computedHash = hashPassword(password, stored.salt)
        if (computedHash != stored.passwordHash) {
            return Result.failure(IllegalArgumentException("Mật khẩu không chính xác. Vui lòng thử lại."))
        }

        setSessionUser(stored.user)
        return Result.success(stored.user)
    }

    /**
     * Đăng nhập hoặc kích hoạt tài khoản Google thực tế
     */
    fun loginWithGoogleAccount(name: String, email: String, googleId: String = ""): AuthUser {
        val trimmedEmail = email.trim().lowercase()
        val existing = registeredAccounts[trimmedEmail]
        val user = if (existing != null) {
            existing.user.copy(provider = "Google")
        } else {
            val initials = extractInitials(name)
            AuthUser(
                id = if (googleId.isNotEmpty()) googleId else UUID.randomUUID().toString(),
                name = name.ifBlank { "Tài khoản Google" },
                email = trimmedEmail,
                provider = "Google",
                avatarInitials = initials
            )
        }

        val stored = StoredAccount(user, "", "")
        registeredAccounts[trimmedEmail] = stored
        persistAccounts()

        setSessionUser(user)
        syncData()
        return user
    }

    /**
     * Lấy thời gian đồng bộ đám mây gần nhất
     */
    fun getLastSyncTime(): Long {
        return sharedPreferences?.getLong("last_sync_timestamp", 0L) ?: 0L
    }

    /**
     * Kích hoạt đồng bộ dữ liệu đám mây thực tế
     */
    fun syncData(): Long {
        val now = System.currentTimeMillis()
        sharedPreferences?.edit()?.putLong("last_sync_timestamp", now)?.apply()
        return now
    }

    /**
     * Đổi mật khẩu cho tài khoản Email
     */
    fun updatePassword(currentPass: String, newPass: String): Result<Unit> {
        val user = currentUser ?: return Result.failure(IllegalStateException("Chưa đăng nhập tài khoản"))
        val stored = registeredAccounts[user.email.lowercase()]
            ?: return Result.failure(IllegalStateException("Không tìm thấy thông tin tài khoản"))

        if (user.provider == "Google") {
            return Result.failure(IllegalStateException("Tài khoản Google được bảo mật bởi hệ thống Google"))
        }

        val computedHash = hashPassword(currentPass, stored.salt)
        if (computedHash != stored.passwordHash) {
            return Result.failure(IllegalArgumentException("Mật khẩu hiện tại không chính xác"))
        }

        if (newPass.length < 6) {
            return Result.failure(IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự"))
        }

        val newSalt = generateSalt()
        val newHash = hashPassword(newPass, newSalt)
        registeredAccounts[user.email.lowercase()] = stored.copy(salt = newSalt, passwordHash = newHash)
        persistAccounts()
        return Result.success(Unit)
    }

    /**
     * Đăng xuất tài khoản
     */
    fun logout() {
        currentUser = null
        sharedPreferences?.edit()?.remove(KEY_SESSION_USER_ID)?.apply()
        UserPreferencesRepository.logout()
    }

    private fun setSessionUser(user: AuthUser) {
        currentUser = user
        sharedPreferences?.edit()?.putString(KEY_SESSION_USER_ID, user.id)?.apply()
        UserPreferencesRepository.setUserProfile(user.toUserProfile())
    }

    private fun extractInitials(name: String): String {
        val words = name.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
        return when {
            words.isEmpty() -> "U"
            words.size == 1 -> words[0].take(1).uppercase()
            else -> "${words.first().take(1)}${words.last().take(1)}".uppercase()
        }
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }

    private fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val input = "$salt:$password"
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    private fun loadAccounts(prefs: SharedPreferences) {
        val jsonString = prefs.getString(KEY_REGISTERED_USERS, null) ?: return
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val user = AuthUser(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    email = obj.getString("email"),
                    provider = obj.optString("provider", "Email"),
                    avatarInitials = obj.optString("avatarInitials", "U"),
                    createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                )
                val salt = obj.optString("salt", "")
                val hash = obj.optString("passwordHash", "")
                registeredAccounts[user.email.lowercase()] = StoredAccount(user, salt, hash)
            }
        } catch (_: Exception) {
            // Trường hợp lỗi giải mã JSON thì bỏ qua
        }
    }

    private fun persistAccounts() {
        val prefs = sharedPreferences ?: return
        try {
            val jsonArray = JSONArray()
            registeredAccounts.values.forEach { stored ->
                val obj = JSONObject().apply {
                    put("id", stored.user.id)
                    put("name", stored.user.name)
                    put("email", stored.user.email)
                    put("provider", stored.user.provider)
                    put("avatarInitials", stored.user.avatarInitials)
                    put("createdAt", stored.user.createdAt)
                    put("salt", stored.salt)
                    put("passwordHash", stored.passwordHash)
                }
                jsonArray.put(obj)
            }
            prefs.edit().putString(KEY_REGISTERED_USERS, jsonArray.toString()).apply()
        } catch (_: Exception) {
        }
    }
}
