package com.flightinfo.app.data.dao

import androidx.room.*
import com.flightinfo.app.data.model.BookmarkedFlight
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedFlightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmarkedFlight: BookmarkedFlight)

    @Delete
    suspend fun delete(bookmarkedFlight: BookmarkedFlight)

    @Query("SELECT * FROM bookmarked_flights")
    fun getAllBookmarkedFlights(): Flow<List<BookmarkedFlight>>

    @Query("SELECT COUNT(*) FROM bookmarked_flights WHERE flightNumber = :flightNumber")
    fun isBookmarked(flightNumber: String): Flow<Int>
}
