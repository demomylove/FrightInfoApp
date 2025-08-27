package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flightinfo.app.data.model.FavoriteRoute
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRouteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteRoute: FavoriteRoute)

    @Query("DELETE FROM favorite_routes WHERE originAirport = :originAirport AND destinationAirport = :destinationAirport")
    suspend fun delete(originAirport: String, destinationAirport: String)

    @Query("SELECT * FROM favorite_routes ORDER BY id DESC")
    fun getAllFavoriteRoutes(): Flow<List<FavoriteRoute>>

    @Query("SELECT COUNT(*) FROM favorite_routes WHERE originAirport = :originAirport AND destinationAirport = :destinationAirport")
    fun isFavorite(originAirport: String, destinationAirport: String): Flow<Int>
}
