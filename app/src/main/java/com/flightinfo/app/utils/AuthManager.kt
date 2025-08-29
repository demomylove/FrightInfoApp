package com.flightinfo.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.flightinfo.app.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val context: Context,
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_preferences", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: Flow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: Flow<Boolean> = _isLoggedIn.asStateFlow()

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    init {
        // 检查是否有已登录的用户
        checkExistingSession()
    }

    private fun checkExistingSession() {
        val userId = sharedPreferences.getLong(KEY_USER_ID, -1)
        val userEmail = sharedPreferences.getString(KEY_USER_EMAIL, null)
        val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)

        if (userId > 0 && userEmail != null && isLoggedIn) {
            // 在实际应用中，这里应该从数据库或其他存储中获取完整的用户信息
            // 现在创建一个简化的用户对象
            val user = User(
                userId = userId,
                email = userEmail,
                passwordHash = "", // 不存储密码哈希
                isActive = true,
            )
            _currentUser.value = user
            _isLoggedIn.value = true
        }
    }

    fun login(user: User) {
        sharedPreferences.edit().apply {
            putLong(KEY_USER_ID, user.userId)
            putString(KEY_USER_EMAIL, user.email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }

        _currentUser.value = user
        _isLoggedIn.value = true
    }

    fun logout() {
        sharedPreferences.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_USER_EMAIL)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }

        _currentUser.value = null
        _isLoggedIn.value = false
    }

    fun getCurrentUserId(): Long? {
        return if (_isLoggedIn.value) {
            sharedPreferences.getLong(KEY_USER_ID, -1).takeIf { it > 0 }
        } else {
            null
        }
    }

    fun getCurrentUserEmail(): String? {
        return if (_isLoggedIn.value) {
            sharedPreferences.getString(KEY_USER_EMAIL, null)
        } else {
            null
        }
    }

    fun updateUser(updatedUser: User) {
        if (_isLoggedIn.value && getCurrentUserId() == updatedUser.userId) {
            _currentUser.value = updatedUser
        }
    }
}
