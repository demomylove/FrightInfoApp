package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flightinfo.app.data.model.BaggageItem
import com.flightinfo.app.data.model.BaggageStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BaggageDao {
    @Query("SELECT * FROM baggage_items ORDER BY last_updated DESC")
    fun getAllBaggageItems(): Flow<List<BaggageItem>>

    @Query("SELECT * FROM baggage_items WHERE id = :id LIMIT 1")
    suspend fun getBaggageItemById(id: Long): BaggageItem?

    @Query("SELECT * FROM baggage_items WHERE baggage_tag_number = :tagNumber LIMIT 1")
    suspend fun getBaggageItemByTagNumber(tagNumber: String): BaggageItem?

    @Query("SELECT * FROM baggage_items WHERE flight_number = :flightNumber")
    fun getBaggageItemsByFlight(flightNumber: String): Flow<List<BaggageItem>>

    @Query("SELECT * FROM baggage_items WHERE status = :status")
    fun getBaggageItemsByStatus(status: BaggageStatus): Flow<List<BaggageItem>>

    @Query("SELECT * FROM baggage_items WHERE status IN (:statuses)")
    fun getBaggageItemsByStatuses(statuses: List<BaggageStatus>): Flow<List<BaggageItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBaggageItem(baggageItem: BaggageItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBaggageItems(baggageItems: List<BaggageItem>)

    @Update
    suspend fun updateBaggageItem(baggageItem: BaggageItem)

    @Delete
    suspend fun deleteBaggageItem(baggageItem: BaggageItem)

    @Query("DELETE FROM baggage_items WHERE id = :id")
    suspend fun deleteBaggageItemById(id: Long)

    @Query("DELETE FROM baggage_items WHERE baggage_tag_number = :tagNumber")
    suspend fun deleteBaggageItemByTagNumber(tagNumber: String)

    @Query("DELETE FROM baggage_items WHERE flight_number = :flightNumber")
    suspend fun deleteBaggageItemsByFlight(flightNumber: String)

    @Query("UPDATE baggage_items SET status = :status, last_updated = :lastUpdated WHERE baggage_tag_number = :tagNumber")
    suspend fun updateBaggageStatus(tagNumber: String, status: BaggageStatus, lastUpdated: Long = System.currentTimeMillis())

    @Query("UPDATE baggage_items SET last_location = :location, last_updated = :lastUpdated WHERE baggage_tag_number = :tagNumber")
    suspend fun updateBaggageLocation(tagNumber: String, location: String, lastUpdated: Long = System.currentTimeMillis())

    @Query("UPDATE baggage_items SET carousel_number = :carouselNumber WHERE baggage_tag_number = :tagNumber AND flight_number = :flightNumber")
    suspend fun updateCarouselNumber(tagNumber: String, flightNumber: String, carouselNumber: String)

    @Query("SELECT COUNT(*) FROM baggage_items WHERE flight_number = :flightNumber AND status = :status")
    suspend fun getBaggageCountByStatus(flightNumber: String, status: BaggageStatus): Int

    @Query("SELECT COUNT(*) FROM baggage_items WHERE status = :status")
    fun getBaggageCountFlowByStatus(status: BaggageStatus): Flow<Int>

    @Query("SELECT COUNT(*) FROM baggage_items WHERE flight_number = :flightNumber")
    suspend fun getTotalBaggageCountForFlight(flightNumber: String): Int

    @Query("SELECT COUNT(*) FROM baggage_items WHERE flight_number = :flightNumber AND status IN ('DELIVERED', 'ON_CAROUSEL')")
    suspend fun getDeliveredBaggageCountForFlight(flightNumber: String): Int
}
