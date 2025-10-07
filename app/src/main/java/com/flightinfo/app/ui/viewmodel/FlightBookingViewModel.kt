package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.FlightBooking
import com.flightinfo.app.data.model.InsuranceOption
import com.flightinfo.app.data.model.MealPreference
import com.flightinfo.app.data.model.Seat
import com.flightinfo.app.data.repository.FlightRepository
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightBookingViewModel @Inject constructor(
    private val repository: FlightRepository,
) : ViewModel() {

    // UI State for Booking
    sealed class BookingUiState {
        object Idle : BookingUiState()
        object Loading : BookingUiState()
        data class Success(val message: String = "Booking successful!") : BookingUiState()
        data class Error(val message: String) : BookingUiState()
    }

    private val _bookingUiState = MutableStateFlow<BookingUiState>(BookingUiState.Idle)
    val bookingUiState: StateFlow<BookingUiState> = _bookingUiState.asStateFlow()

    // Total price state
    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _basePrice = MutableStateFlow(0.0)
    val basePrice: StateFlow<Double> = _basePrice.asStateFlow()

    private val _currency = MutableStateFlow("CNY")
    val currency: StateFlow<String> = _currency.asStateFlow()

    fun bookFlight(flightId: String, bookingInfo: FlightBooking) {
        viewModelScope.launch {
            _bookingUiState.value = BookingUiState.Loading
            repository.bookFlight(flightId, bookingInfo)
                .collect { resource ->
                    when (resource) {
                        is Resource.Idle<*> -> {
                            // Initial state, do nothing
                        }
                        is Resource.Success<*> -> {
                            _bookingUiState.value = BookingUiState.Success()
                        }
                        is Resource.Error<*> -> {
                            _bookingUiState.value = BookingUiState.Error(resource.message ?: "An unknown error occurred.")
                        }
                        is Resource.Loading<*> -> {
                            // Loading state is already set at the beginning of the function
                        }
                    }
                }
        }
    }

    /**
     * 加载航班基础票价
     */
    fun loadFlightPrice(flightId: String) {
        viewModelScope.launch {
            repository.getFlightPrice(flightId).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val priceInfo = resource.data
                        if (priceInfo != null) {
                            _basePrice.value = priceInfo.price
                            _currency.value = priceInfo.currency
                            // 初始化总价为基础票价
                            _totalPrice.value = priceInfo.price
                        }
                    }
                    is Resource.Error -> {
                        // 保持默认价格0，交给UI提示或继续计算附加项
                    }
                    else -> Unit
                }
            }
        }
    }

    /**
     * 验证预订信息
     * @param bookingInfo 预订信息
     * @return 验证结果，如果为null表示验证通过，否则返回错误消息
     */
    fun validateBookingInfo(bookingInfo: FlightBooking): String? {
        if (bookingInfo.passengerName.isBlank()) {
            return "请输入乘客姓名"
        }

        if (bookingInfo.passengerEmail.isBlank()) {
            return "请输入乘客邮箱"
        }

        if (bookingInfo.passengerPhone.isBlank()) {
            return "请输入乘客电话"
        }

        if (bookingInfo.seat.isBlank()) {
            return "请选择座位"
        }

        // 验证邮箱格式
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(bookingInfo.passengerEmail).matches()) {
            return "请输入有效的邮箱地址"
        }

        // 验证身份证号格式（简单验证）
        if (bookingInfo.passengerId.isNotBlank() && bookingInfo.passengerId.length != 18) {
            return "请输入有效的身份证号"
        }

        return null // 验证通过
    }

    /**
     * 计算总价
     * @param basePrice 基础票价
     * @param seat 座位信息
     * @param mealPreference 餐食选择
     * @param insuranceOption 保险选项
     * @param baggageInfo 行李信息
     */
    fun calculateTotalPrice(
        basePrice: Double,
        seat: Seat? = null,
        mealPreference: MealPreference = MealPreference.NONE,
        insuranceOption: InsuranceOption = InsuranceOption.NONE,
        baggageInfo: com.flightinfo.app.data.model.BaggageInfo = com.flightinfo.app.data.model.BaggageInfo(),
    ) {
        var total = basePrice

        // 添加座位费用
        seat?.let {
            total += it.price
        }

        // 添加餐食费用
        val mealPrice = when (mealPreference) {
            MealPreference.NONE -> 0.0
            MealPreference.STANDARD -> 20.0
            MealPreference.VEGETARIAN -> 25.0
            MealPreference.VEGAN -> 30.0
            MealPreference.GLUTEN_FREE -> 35.0
            MealPreference.CHILD -> 15.0
            MealPreference.DIABETIC -> 40.0
        }
        total += mealPrice

        // 添加保险费用
        val insurancePrice = when (insuranceOption) {
            InsuranceOption.NONE -> 0.0
            InsuranceOption.BASIC -> 50.0
            InsuranceOption.COMPREHENSIVE -> 100.0
            InsuranceOption.PREMIUM -> 200.0
        }
        total += insurancePrice

        // 添加行李费用
        val baggagePrice = baggageInfo.checkedBaggage * 30.0 // 每件托运行李30元
        total += baggagePrice

        _totalPrice.value = total
    }
}
