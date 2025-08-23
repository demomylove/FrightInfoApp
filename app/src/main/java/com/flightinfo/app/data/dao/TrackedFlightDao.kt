package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flightinfo.app.data.model.TrackedFlight
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackedFlightDao {
    @Query("SELECT * FROM tracked_flights")
    fun getAllTrackedFlights(): Flow<List<TrackedFlight>>

    @Query("SELECT * FROM tracked_flights WHERE flightId = :flightId LIMIT 1")
    suspend fun getTrackedFlightById(flightId: String): TrackedFlight?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedFlight(trackedFlight: TrackedFlight)

    @Update
    suspend fun updateTrackedFlight(trackedFlight: TrackedFlight)

    @Delete
    suspend fun deleteTrackedFlight(trackedFlight: TrackedFlight)

    @Query("DELETE FROM tracked_flights WHERE flightId = :flightId")
    suspend fun deleteTrackedFlightById(flightId: String)
}