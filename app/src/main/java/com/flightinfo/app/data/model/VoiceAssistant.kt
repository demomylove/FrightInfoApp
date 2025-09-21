package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "voice_commands")
data class VoiceCommand(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val command: String,
    val intent: VoiceIntent,
    val parameters: Map<String, String> = emptyMap(),
    val timestamp: Date = Date(),
    val success: Boolean = false,
    val errorMessage: String? = null,
)

enum class VoiceIntent {
    FLIGHT_SEARCH,
    FLIGHT_STATUS,
    AIRPORT_INFO,
    SET_REMINDER,
    BOOKING_INFO,
    WEATHER_INFO,
    GENERAL_HELP,
    UNKNOWN,
}

data class VoiceRecognitionResult(
    val text: String,
    val confidence: Float,
    val intent: VoiceIntent,
    val parameters: Map<String, String> = emptyMap(),
)

data class VoiceSettings(
    val id: Int = 1,
    val isEnabled: Boolean = true,
    val language: String = "zh-CN",
    val wakeWordEnabled: Boolean = false,
    val autoResponseEnabled: Boolean = true,
    val voiceFeedbackEnabled: Boolean = true,
)

data class VoiceCommandHistory(
    val command: String,
    val timestamp: Date,
    val success: Boolean,
    val response: String? = null,
)
