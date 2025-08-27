package com.flightinfo.app.data.repository

import com.flightinfo.app.data.dao.FavoriteRouteDao
import com.flightinfo.app.data.model.FavoriteRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRouteRepository @Inject constructor(
    private val favoriteRouteDao: FavoriteRouteDao,
) {

    fun getAllFavoriteRoutes(): Flow<List<FavoriteRoute>> {
        return favoriteRouteDao.getAllFavoriteRoutes()
    }

    fun isFavorite(originAirport: String, destinationAirport: String): Flow<Boolean> {
        return favoriteRouteDao.isFavorite(originAirport, destinationAirport).map { it > 0 }
    }

    suspend fun addFavorite(originAirport: String, destinationAirport: String) {
        val favoriteRoute = FavoriteRoute(
            originAirport = originAirport,
            destinationAirport = destinationAirport,
        )
        favoriteRouteDao.insert(favoriteRoute)
    }

    suspend fun removeFavorite(originAirport: String, destinationAirport: String) {
        favoriteRouteDao.delete(originAirport, destinationAirport)
    }
}
