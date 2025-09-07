package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "itineraries")
data class Itinerary(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String, // 行程标题（如：北京出差）
    val pnr: String? = null, // 订座记录号
    val passengers: List<String> = emptyList(), // 乘客姓名列表
    val startTime: Long, // 行程开始（第一段起飞）
    val endTime: Long, // 行程结束（最后一段到达）
    val segments: List<ItinerarySegment> = emptyList(), // 多航段
    val tasks: List<TripTask> = emptyList(), // 待办
    val checkInUrl: String? = null, // 值机链接
    val boardingPassUrl: String? = null, // 登机牌链接
)

data class ItinerarySegment(
    val flightNumber: String,
    val airline: String?,
    val departureAirport: String,
    val arrivalAirport: String,
    val departureTime: Long, // 使用UTC毫秒
    val arrivalTime: Long,
    val gate: String? = null,
    val terminal: String? = null,
)

data class TripTask(
    val id: String, // 客户端生成的UUID
    val title: String, // 任务名（如 提前3小时出发 前往机场）
    val dueTime: Long, // 截止时间/提醒时间
    val completed: Boolean = false,
)

class ItineraryConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromSegments(value: String?): List<ItinerarySegment> =
        if (value.isNullOrEmpty()) emptyList() else gson.fromJson(value, object : TypeToken<List<ItinerarySegment>>() {}.type)

    @TypeConverter
    fun toSegments(list: List<ItinerarySegment>?): String = gson.toJson(list ?: emptyList<ItinerarySegment>())

    @TypeConverter
    fun fromTasks(value: String?): List<TripTask> =
        if (value.isNullOrEmpty()) emptyList() else gson.fromJson(value, object : TypeToken<List<TripTask>>() {}.type)

    @TypeConverter
    fun toTasks(list: List<TripTask>?): String = gson.toJson(list ?: emptyList<TripTask>())
}
