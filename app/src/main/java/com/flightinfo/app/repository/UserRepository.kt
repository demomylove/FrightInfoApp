package com.flightinfo.app.repository

import com.flightinfo.app.data.dao.UserDao
import com.flightinfo.app.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    // TODO: Replace with a more secure hashing algorithm like bcrypt
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Pattern.compile(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+",
        )
        return emailRegex.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    suspend fun registerUser(
        email: String,
        password: String,
        firstName: String = "",
        lastName: String = "",
        phoneNumber: String = "",
    ): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                // 验证输入
                if (!isValidEmail(email)) {
                    return@withContext Result.failure(Exception("Invalid email format"))
                }
                if (!isValidPassword(password)) {
                    return@withContext Result.failure(Exception("Password must be at least 6 characters"))
                }

                // 检查邮箱是否已存在
                if (userDao.emailExists(email) > 0) {
                    return@withContext Result.failure(Exception("User with this email already exists"))
                }

                // 创建新用户
                val passwordHash = hashPassword(password)
                val newUser = User(
                    email = email,
                    passwordHash = passwordHash,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                )

                val userId = userDao.insert(newUser)
                if (userId > 0) {
                    val createdUser = newUser.copy(userId = userId)
                    Result.success(createdUser)
                } else {
                    Result.failure(Exception("Failed to create user"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserByEmail(email)
                if (user != null && user.passwordHash == hashPassword(password)) {
                    if (!user.isActive) {
                        return@withContext Result.failure(Exception("Account is deactivated"))
                    }

                    // 更新最后登录时间
                    userDao.updateLastLogin(user.userId, System.currentTimeMillis())
                    Result.success(user)
                } else {
                    Result.failure(Exception("Invalid email or password"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUserById(userId: Long): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserById(userId)
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User not found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateUserProfile(
        userId: Long,
        firstName: String? = null,
        lastName: String? = null,
        phoneNumber: String? = null,
    ): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = userDao.getUserById(userId)
                    ?: return@withContext Result.failure(Exception("User not found"))

                val updatedUser = currentUser.copy(
                    firstName = firstName ?: currentUser.firstName,
                    lastName = lastName ?: currentUser.lastName,
                    phoneNumber = phoneNumber ?: currentUser.phoneNumber,
                )

                userDao.updateUser(updatedUser)
                Result.success(updatedUser)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun changePassword(userId: Long, oldPassword: String, newPassword: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserById(userId)
                    ?: return@withContext Result.failure(Exception("User not found"))

                if (user.passwordHash != hashPassword(oldPassword)) {
                    return@withContext Result.failure(Exception("Current password is incorrect"))
                }

                if (!isValidPassword(newPassword)) {
                    return@withContext Result.failure(Exception("New password must be at least 6 characters"))
                }

                val updatedUser = user.copy(passwordHash = hashPassword(newPassword))
                userDao.updateUser(updatedUser)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deactivateAccount(userId: Long): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                userDao.updateUserStatus(userId, false)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteAccount(userId: Long): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                userDao.deleteUser(userId)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun getCurrentUser(): Flow<User?> = flow {
        // 在实际应用中，这里应该从SharedPreferences或安全存储中获取当前用户ID
        // 现在返回null作为占位符
        emit(null)
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            // 清除会话数据、令牌等
            // 在实际应用中，这里应该清除SharedPreferences中的用户信息
        }
    }
}
