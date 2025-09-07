package com.flightinfo.app.data.repository

import com.flightinfo.app.data.dao.BaggageDao
import com.flightinfo.app.data.model.BaggageItem
import com.flightinfo.app.data.model.BaggageStatus
import com.flightinfo.app.data.model.BaggageTrackingRequest
import com.flightinfo.app.data.model.BaggageTrackingResponse
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaggageRepository @Inject constructor(
    private val baggageDao: BaggageDao,
) {
    fun getAllBaggageItems(): Flow<List<BaggageItem>> {
        return baggageDao.getAllBaggageItems()
    }

    suspend fun getBaggageItemById(id: Long): BaggageItem? {
        return baggageDao.getBaggageItemById(id)
    }

    suspend fun getBaggageItemByTagNumber(tagNumber: String): BaggageItem? {
        return baggageDao.getBaggageItemByTagNumber(tagNumber)
    }

    fun getBaggageItemsByFlight(flightNumber: String): Flow<List<BaggageItem>> {
        return baggageDao.getBaggageItemsByFlight(flightNumber)
    }

    fun getBaggageItemsByStatus(status: BaggageStatus): Flow<List<BaggageItem>> {
        return baggageDao.getBaggageItemsByStatus(status)
    }

    fun getActiveBaggageItems(): Flow<List<BaggageItem>> {
        return baggageDao.getBaggageItemsByStatuses(
            listOf(
                BaggageStatus.CHECKED_IN,
                BaggageStatus.LOADED,
                BaggageStatus.IN_TRANSIT,
                BaggageStatus.UNLOADED,
                BaggageStatus.ON_CAROUSEL,
            ),
        )
    }

    suspend fun insertBaggageItem(baggageItem: BaggageItem): Long {
        return baggageDao.insertBaggageItem(baggageItem)
    }

    suspend fun insertBaggageItems(baggageItems: List<BaggageItem>) {
        baggageDao.insertBaggageItems(baggageItems)
    }

    suspend fun updateBaggageItem(baggageItem: BaggageItem) {
        baggageDao.updateBaggageItem(baggageItem)
    }

    suspend fun deleteBaggageItem(baggageItem: BaggageItem) {
        baggageDao.deleteBaggageItem(baggageItem)
    }

    suspend fun deleteBaggageItemById(id: Long) {
        baggageDao.deleteBaggageItemById(id)
    }

    suspend fun deleteBaggageItemByTagNumber(tagNumber: String) {
        baggageDao.deleteBaggageItemByTagNumber(tagNumber)
    }

    suspend fun deleteBaggageItemsByFlight(flightNumber: String) {
        baggageDao.deleteBaggageItemsByFlight(flightNumber)
    }

    suspend fun updateBaggageStatus(tagNumber: String, status: BaggageStatus) {
        baggageDao.updateBaggageStatus(tagNumber, status, System.currentTimeMillis())
    }

    suspend fun updateBaggageLocation(tagNumber: String, location: String) {
        baggageDao.updateBaggageLocation(tagNumber, location, System.currentTimeMillis())
    }

    suspend fun updateCarouselNumber(tagNumber: String, flightNumber: String, carouselNumber: String) {
        baggageDao.updateCarouselNumber(tagNumber, flightNumber, carouselNumber)
    }

    suspend fun getBaggageStatisticsForFlight(flightNumber: String): Map<BaggageStatus, Int> {
        val statistics = mutableMapOf<BaggageStatus, Int>()
        BaggageStatus.values().forEach { status ->
            statistics[status] = baggageDao.getBaggageCountByStatus(flightNumber, status)
        }
        return statistics
    }

    fun getBaggageCountFlowByStatus(status: BaggageStatus): Flow<Int> {
        return baggageDao.getBaggageCountFlowByStatus(status)
    }

    suspend fun getTotalBaggageCountForFlight(flightNumber: String): Int {
        return baggageDao.getTotalBaggageCountForFlight(flightNumber)
    }

    suspend fun getDeliveredBaggageCountForFlight(flightNumber: String): Int {
        return baggageDao.getDeliveredBaggageCountForFlight(flightNumber)
    }

    suspend fun trackBaggage(request: BaggageTrackingRequest): Result<BaggageTrackingResponse> {
        return try {
            // 模拟API调用 - 实际应用中这里会调用真实的行李跟踪API
            val mockBaggageItem = BaggageItem(
                baggageTagNumber = request.baggageTagNumber,
                flightNumber = request.flightNumber,
                baggageType = com.flightinfo.app.data.model.BaggageType.CHECKED,
                weight = 23.5,
                weightUnit = "kg",
                status = BaggageStatus.CHECKED_IN,
                lastLocation = "机场行李柜台",
                checkInTime = Date(),
                loadedTime = null,
                unloadedTime = null,
                carouselNumber = null,
                specialHandling = false,
                notes = "乘客: ${request.passengerName}",
            )

            val response = BaggageTrackingResponse(
                baggageItems = listOf(mockBaggageItem),
                flightInfo = null,
                airportInfo = null,
            )

            // 保存到本地数据库
            insertBaggageItem(mockBaggageItem)

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshBaggageStatus(tagNumber: String): Result<BaggageItem> {
        return try {
            // 模拟API调用 - 实际应用中这里会调用真实的行李状态更新API
            val existingItem = getBaggageItemByTagNumber(tagNumber)
            existingItem?.let { item ->
                val updatedStatus = when (item.status) {
                    BaggageStatus.CHECKED_IN -> BaggageStatus.LOADED
                    BaggageStatus.LOADED -> BaggageStatus.IN_TRANSIT
                    BaggageStatus.IN_TRANSIT -> BaggageStatus.UNLOADED
                    BaggageStatus.UNLOADED -> BaggageStatus.ON_CAROUSEL
                    BaggageStatus.ON_CAROUSEL -> BaggageStatus.DELIVERED
                    else -> item.status
                }

                val updatedItem = item.copy(
                    status = updatedStatus,
                    lastUpdated = Date(),
                    lastLocation = when (updatedStatus) {
                        BaggageStatus.LOADED -> "已装载至飞机"
                        BaggageStatus.IN_TRANSIT -> "运输中"
                        BaggageStatus.UNLOADED -> "已卸载至目的地"
                        BaggageStatus.ON_CAROUSEL -> "行李转盘"
                        BaggageStatus.DELIVERED -> "已交付"
                        else -> item.lastLocation
                    },
                )

                updateBaggageItem(updatedItem)
                Result.success(updatedItem)
            } ?: Result.failure(Exception("Baggage item not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
