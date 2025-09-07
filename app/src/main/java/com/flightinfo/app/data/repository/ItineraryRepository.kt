package com.flightinfo.app.data.repository

import com.flightinfo.app.data.dao.ItineraryDao
import com.flightinfo.app.data.model.Itinerary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItineraryRepository @Inject constructor(
    private val itineraryDao: ItineraryDao,
) {
    fun getAll(): Flow<List<Itinerary>> = itineraryDao.getAllItineraries()

    fun getById(id: Long): Flow<Itinerary?> = itineraryDao.getItineraryById(id)

    suspend fun add(itinerary: Itinerary): Long = itineraryDao.insert(itinerary)

    suspend fun update(itinerary: Itinerary) = itineraryDao.update(itinerary)

    suspend fun delete(itinerary: Itinerary) = itineraryDao.delete(itinerary)
}
