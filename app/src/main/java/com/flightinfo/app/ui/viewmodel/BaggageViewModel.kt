package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.BaggageItem
import com.flightinfo.app.data.model.BaggageStatus
import com.flightinfo.app.data.model.BaggageTrackingRequest
import com.flightinfo.app.data.model.BaggageTrackingResponse
import com.flightinfo.app.data.repository.BaggageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaggageViewModel @Inject constructor(
    private val baggageRepository: BaggageRepository,
) : ViewModel() {

    private val _baggageItems = MutableStateFlow<List<BaggageItem>>(emptyList())
    val baggageItems: StateFlow<List<BaggageItem>> = _baggageItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _trackingResult = MutableStateFlow<Result<BaggageTrackingResponse>?>(null)
    val trackingResult: StateFlow<Result<BaggageTrackingResponse>?> = _trackingResult.asStateFlow()

    init {
        viewModelScope.launch {
            baggageRepository.getAllBaggageItems().collect { items ->
                _baggageItems.value = items
            }
        }
    }

    fun getAllBaggageItems() {
        viewModelScope.launch {
            baggageRepository.getAllBaggageItems().collect { items ->
                _baggageItems.value = items
            }
        }
    }

    fun getBaggageItemsByFlight(flightNumber: String) {
        viewModelScope.launch {
            baggageRepository.getBaggageItemsByFlight(flightNumber).collect { items ->
                _baggageItems.value = items
            }
        }
    }

    fun getActiveBaggageItems() {
        viewModelScope.launch {
            baggageRepository.getActiveBaggageItems().collect { items ->
                _baggageItems.value = items
            }
        }
    }

    fun trackBaggage(request: BaggageTrackingRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = baggageRepository.trackBaggage(request)
                _trackingResult.value = result

                if (result.isFailure) {
                    _error.value = result.exceptionOrNull()?.message ?: "跟踪行李失败"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "跟踪行李失败"
                _trackingResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshBaggageStatus(tagNumber: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = baggageRepository.refreshBaggageStatus(tagNumber)
                if (result.isFailure) {
                    _error.value = result.exceptionOrNull()?.message ?: "刷新行李状态失败"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "刷新行李状态失败"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addBaggageItem(baggageItem: BaggageItem) {
        viewModelScope.launch {
            try {
                baggageRepository.insertBaggageItem(baggageItem)
            } catch (e: Exception) {
                _error.value = e.message ?: "添加行李失败"
            }
        }
    }

    fun updateBaggageItem(baggageItem: BaggageItem) {
        viewModelScope.launch {
            try {
                baggageRepository.updateBaggageItem(baggageItem)
            } catch (e: Exception) {
                _error.value = e.message ?: "更新行李失败"
            }
        }
    }

    fun deleteBaggageItem(baggageItem: BaggageItem) {
        viewModelScope.launch {
            try {
                baggageRepository.deleteBaggageItem(baggageItem)
            } catch (e: Exception) {
                _error.value = e.message ?: "删除行李失败"
            }
        }
    }

    fun updateBaggageStatus(tagNumber: String, status: BaggageStatus) {
        viewModelScope.launch {
            try {
                baggageRepository.updateBaggageStatus(tagNumber, status)
            } catch (e: Exception) {
                _error.value = e.message ?: "更新行李状态失败"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearTrackingResult() {
        _trackingResult.value = null
    }

    fun getBaggageStatistics(flightNumber: String) {
        viewModelScope.launch {
            try {
                baggageRepository.getBaggageStatisticsForFlight(flightNumber)
            } catch (e: Exception) {
                _error.value = e.message ?: "获取行李统计失败"
            }
        }
    }

    fun createSampleBaggageIfEmpty() {
        if (_baggageItems.value.isNotEmpty()) return

        viewModelScope.launch {
            val sampleBaggage = listOf(
                BaggageItem(
                    baggageTagNumber = "CA123456",
                    flightNumber = "MU5123",
                    baggageType = com.flightinfo.app.data.model.BaggageType.CHECKED,
                    weight = 23.5,
                    weightUnit = "kg",
                    status = BaggageStatus.CHECKED_IN,
                    lastLocation = "北京首都机场T2行李柜台",
                    checkInTime = java.util.Date(),
                    loadedTime = null,
                    unloadedTime = null,
                    carouselNumber = null,
                    specialHandling = false,
                    notes = "红色行李箱",
                ),
                BaggageItem(
                    baggageTagNumber = "CA123457",
                    flightNumber = "MU5123",
                    baggageType = com.flightinfo.app.data.model.BaggageType.CHECKED,
                    weight = 18.0,
                    weightUnit = "kg",
                    status = BaggageStatus.LOADED,
                    lastLocation = "已装载至飞机",
                    checkInTime = java.util.Date(System.currentTimeMillis() - 3600000),
                    loadedTime = java.util.Date(),
                    unloadedTime = null,
                    carouselNumber = null,
                    specialHandling = false,
                    notes = "黑色行李箱",
                ),
            )

            baggageRepository.insertBaggageItems(sampleBaggage)
        }
    }
}
