package com.flightinfo.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flightinfo.app.R
import com.flightinfo.app.data.model.FlightBooking
import com.flightinfo.app.data.model.InsuranceOption
import com.flightinfo.app.data.model.MealPreference
import com.flightinfo.app.data.model.Seat
import com.flightinfo.app.databinding.FragmentFlightBookingBinding
import com.flightinfo.app.ui.WeatherActivity
import com.flightinfo.app.ui.dialog.SeatSelectionDialog
import com.flightinfo.app.ui.viewmodel.FlightBookingViewModel
import com.flightinfo.app.ui.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlightBookingFragment : Fragment() {

    private var _binding: FragmentFlightBookingBinding? = null
    val binding get() = _binding!!

    private val viewModel: FlightBookingViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()
    private var selectedSeat: Seat? = null
    private var flightNumber: String? = null
    private var departureAirport: String? = null
    private var arrivalAirport: String? = null
    private var flightDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFlightBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 获取航班信息
        flightNumber = arguments?.getString("flightNumber")
        departureAirport = arguments?.getString("departureAirport")
        arrivalAirport = arguments?.getString("arrivalAirport")
        flightDate = arguments?.getString("flightDate")

        // 加载航班天气信息
        loadFlightWeather()

        // 设置座位选择按钮点击事件
        binding.selectSeatButton.setOnClickListener {
            val seatSelectionDialog = SeatSelectionDialog(requireContext()) { seat ->
                selectedSeat = seat
                binding.seatEditText.setText(seat.seatNumber)
            }
            seatSelectionDialog.show()
        }

        // 设置查看天气详情按钮点击事件
        binding.viewWeatherDetailsButton.setOnClickListener {
            val intent = Intent(requireContext(), WeatherActivity::class.java).apply {
                putExtra("departureAirport", departureAirport)
                putExtra("arrivalAirport", arrivalAirport)
                putExtra("flightNumber", flightNumber)
                putExtra("flightDate", flightDate)
            }
            startActivity(intent)
        }

        // 设置餐食选择下拉菜单
        val mealOptions = arrayOf(
            "无餐食",
            "标准餐",
            "素食",
            "纯素食",
            "无麸质",
            "儿童餐",
            "糖尿病餐",
        )
        val mealAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mealOptions)
        mealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mealPreferenceSpinner.adapter = mealAdapter

        // 设置行李信息数字选择器
        setupBaggagePickers()

        // 设置保险选项单选按钮组
        binding.insuranceRadioGroup.setOnCheckedChangeListener { _, _ ->
            // 处理保险选项选择
        }

        binding.bookButton.setOnClickListener {
            val flightId = arguments?.getString("flightId") ?: ""
            val passengerName = binding.nameEditText.text.toString()
            val passengerEmail = binding.emailEditText.text.toString()
            val passengerPhone = binding.phoneEditText.text.toString()
            val passengerId = binding.passengerIdEditText.text.toString()
            val seat = binding.seatEditText.text.toString()
            val specialRequests = binding.specialRequestsEditText.text.toString()
            val specialBaggage = binding.specialBaggageEditText.text.toString()

            if (passengerName.isBlank() || passengerEmail.isBlank() || passengerPhone.isBlank() || seat.isBlank()) {
                Snackbar.make(binding.root, "请填写所有必填字段", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 获取餐食选择
            val mealPreference = when (binding.mealPreferenceSpinner.selectedItemPosition) {
                0 -> MealPreference.NONE
                1 -> MealPreference.STANDARD
                2 -> MealPreference.VEGETARIAN
                3 -> MealPreference.VEGAN
                4 -> MealPreference.GLUTEN_FREE
                5 -> MealPreference.CHILD
                6 -> MealPreference.DIABETIC
                else -> MealPreference.NONE
            }

            // 获取保险选项
            val insuranceOption = when (binding.insuranceRadioGroup.checkedRadioButtonId) {
                R.id.noInsuranceRadioButton -> InsuranceOption.NONE
                R.id.basicInsuranceRadioButton -> InsuranceOption.BASIC
                R.id.comprehensiveInsuranceRadioButton -> InsuranceOption.COMPREHENSIVE
                R.id.premiumInsuranceRadioButton -> InsuranceOption.PREMIUM
                else -> InsuranceOption.NONE
            }

            // 获取行李信息
            val baggageInfo = com.flightinfo.app.data.model.BaggageInfo(
                checkedBaggage = binding.checkedBaggagePicker.value,
                cabinBaggage = binding.cabinBaggagePicker.value,
                specialBaggage = specialBaggage,
            )

            val bookingInfo = FlightBooking(
                flightId = flightId,
                passengerName = passengerName,
                passengerEmail = passengerEmail,
                passengerPhone = passengerPhone,
                seat = seat,
                passengerId = passengerId,
                specialRequests = specialRequests,
                baggageInfo = baggageInfo,
                mealPreference = mealPreference,
                insuranceOption = insuranceOption,
            )
            viewModel.bookFlight(flightId, bookingInfo)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookingUiState.collect { state ->
                when (state) {
                    is FlightBookingViewModel.BookingUiState.Idle -> {
                        // Initial state, do nothing or reset UI
                        binding.progressBar.visibility = View.GONE
                        binding.bookButton.isEnabled = true
                    }
                    is FlightBookingViewModel.BookingUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.bookButton.isEnabled = false
                    }
                    is FlightBookingViewModel.BookingUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.bookButton.isEnabled = true
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        // Optionally, navigate away or clear form
                    }
                    is FlightBookingViewModel.BookingUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.bookButton.isEnabled = true
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun loadFlightWeather() {
        if (flightNumber != null && departureAirport != null && arrivalAirport != null && flightDate != null) {
            weatherViewModel.getFlightWeather(
                flightNumber!!,
                departureAirport!!,
                arrivalAirport!!,
                flightDate!!,
            )

            // 观察天气数据变化
            viewLifecycleOwner.lifecycleScope.launch {
                weatherViewModel.flightWeather.collect { resource ->
                    when (resource) {
                        is com.flightinfo.app.utils.Resource.Idle -> {
                            // Initial state, do nothing
                        }
                        is com.flightinfo.app.utils.Resource.Loading -> {
                            // 显示加载状态
                            binding.departureWeatherInfo.text = "正在加载天气信息..."
                            binding.arrivalWeatherInfo.text = "正在加载天气信息..."
                            binding.weatherImpactInfo.text = "正在加载天气影响信息..."
                        }
                        is com.flightinfo.app.utils.Resource.Success -> {
                            resource.data?.let { flightWeather ->
                                // 更新出发地天气信息
                                binding.departureWeatherInfo.text =
                                    "${flightWeather.departureWeather.location}: " +
                                    "${flightWeather.departureWeather.temperature}°C, " +
                                    flightWeather.departureWeather.condition

                                // 更新目的地天气信息
                                binding.arrivalWeatherInfo.text =
                                    "${flightWeather.arrivalWeather.location}: " +
                                    "${flightWeather.arrivalWeather.temperature}°C, " +
                                    flightWeather.arrivalWeather.condition

                                // 更新天气影响信息
                                val delayProbability = (flightWeather.weatherImpact.delayProbability * 100).toInt()
                                binding.weatherImpactInfo.text =
                                    "延误概率: $delayProbability%, " +
                                    "预计延误: ${flightWeather.weatherImpact.delayMinutes}分钟\n" +
                                    "建议: ${flightWeather.weatherImpact.recommendation}"
                            }
                        }
                        is com.flightinfo.app.utils.Resource.Error -> {
                            // 显示错误信息
                            binding.departureWeatherInfo.text = "无法获取天气信息"
                            binding.arrivalWeatherInfo.text = "无法获取天气信息"
                            binding.weatherImpactInfo.text = "无法获取天气影响信息"
                        }
                    }
                }
            }
        }
    }

    private fun setupBaggagePickers() {
        // 设置托运行李数量选择器
        binding.checkedBaggagePicker.minValue = 0
        binding.checkedBaggagePicker.maxValue = 5
        binding.checkedBaggagePicker.value = 0

        // 设置随身行李数量选择器
        binding.cabinBaggagePicker.minValue = 0
        binding.cabinBaggagePicker.maxValue = 2
        binding.cabinBaggagePicker.value = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
