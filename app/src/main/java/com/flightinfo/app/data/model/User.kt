package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val email: String,
    val passwordHash: String,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = 0,
    val isActive: Boolean = true,
)
