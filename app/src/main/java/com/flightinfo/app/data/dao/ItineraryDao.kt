package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flightinfo.app.data.model.Itinerary
import kotlinx.coroutines.flow.Flow

@Dao
interface ItineraryDao {
    @Query("SELECT * FROM itineraries ORDER BY startTime ASC")
    fun getAllItineraries(): Flow<List<Itinerary>>

    @Query("SELECT * FROM itineraries WHERE id = :id LIMIT 1")
    fun getItineraryById(id: Long): Flow<Itinerary?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itinerary: Itinerary): Long

    @Update
    suspend fun update(itinerary: Itinerary)

    @Delete
    suspend fun delete(itinerary: Itinerary)
}
