package com.flightinfo.app.data.repository

import com.flightinfo.app.data.api.FlightApiService
import com.flightinfo.app.data.model.FlightBooking
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.FlightPriceInfo
import com.flightinfo.app.data.model.FlightSearchResponse
import com.flightinfo.app.data.model.TravelSuggestionResponse
import com.flightinfo.app.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlightRepository @Inject constructor(
    private val apiService: FlightApiService,
) {

    /**
     * 根据航班号、出发机场、到达机场和日期搜索航班。
     * @param flightNumber 航班号
     * @param departureAirport 出发机场
     * @param arrivalAirport 到达机场
     * @param date 日期
     * @return 返回一个包含航班搜索结果的 Flow。
     */
    fun searchFlights(
        flightNumber: String? = null,
        departureAirport: String? = null,
        arrivalAirport: String? = null,
        date: String? = null,
    ): Flow<Resource<FlightSearchResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.searchFlights(flightNumber, departureAirport, arrivalAirport, date)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.message}"))
        }
    }

    /**
     * 获取实时航班信息。
     * @return 返回一个包含实时航班信息的 Flow。
     */
    fun getRealtimeFlights(): Flow<Resource<FlightSearchResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getRealtimeFlights()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error loading real-time flights: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error loading real-time flights: ${e.message}"))
        }
    }

    /**
     * 根据航班号获取航班详细信息。
     * @param flightNumber 航班号
     * @return 返回一个包含航班详细信息的 Flow。
     */
    fun getFlightDetails(flightNumber: String): Flow<Resource<FlightInfo>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getFlightDetails(flightNumber)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error loading flight details: ${e.message}"))
        }
    }

    /**
     * 根据目的地获取旅行建议。
     * @param destination 目的地
     * @return 返回一个包含旅行建议的 Flow。
     */
    fun getTravelSuggestions(destination: String): Flow<Resource<TravelSuggestionResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getTravelSuggestions(destination)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error loading travel suggestions: ${e.message}"))
        }
    }

    /**
     * 预订航班。
     * @param flightId 航班ID
     * @param bookingInfo 预订信息
     * @return 返回一个包含预订结果的 Flow。
     */
    fun bookFlight(flightId: String, bookingInfo: FlightBooking): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.bookFlight(flightId, bookingInfo)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.message}"))
        }
    }

    /**
     * 获取航班价格信息。
     * @param flightId 航班ID
     * @return 返回一个包含航班价格信息的 Flow。
     */
    fun getFlightPrice(flightId: String): Flow<Resource<FlightPriceInfo>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getFlightPrice(flightId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.message}"))
        }
    }
}
