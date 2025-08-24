package com.flightinfo.app.data.database

import androidx.room.TypeConverter
import com.flightinfo.app.data.model.PriceUpdate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPriceUpdateList(value: String?): List<PriceUpdate>? {
        val listType = object : TypeToken<List<PriceUpdate>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toPriceUpdateList(list: List<PriceUpdate>?): String? {
        return Gson().toJson(list)
    }
}
