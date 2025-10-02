package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.flightinfo.app.data.model.WeatherInfo
import com.flightinfo.app.databinding.FragmentWeatherBinding
import com.flightinfo.app.ui.adapter.ForecastAdapter
import com.flightinfo.app.ui.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeWeatherData()
    }

    private fun setupRecyclerView() {
        forecastAdapter = ForecastAdapter()
        binding.forecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = forecastAdapter
        }
    }

    private fun setupClickListeners() {
        binding.searchButton.setOnClickListener {
            val location = binding.locationEditText.text.toString().trim()
            if (location.isNotEmpty()) {
                viewModel.getCurrentWeather(location)
                viewModel.getWeatherForecast(location)
            } else {
                Toast.makeText(requireContext(), "请输入城市名称", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeWeatherData() {
        viewModel.isLoading.onEach { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.weatherScrollView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.currentWeather.onEach { resource ->
            when (resource) {
                is com.flightinfo.app.utils.Resource.Idle -> {
                    // Initial state, do nothing
                }
                is com.flightinfo.app.utils.Resource.Loading -> {
                    // Loading state handled by isLoading
                }
                is com.flightinfo.app.utils.Resource.Success -> {
                    resource.data?.let { weatherResponse ->
                        displayCurrentWeather(weatherResponse.current)
                    }
                }
                is com.flightinfo.app.utils.Resource.Error -> {
                    Snackbar.make(
                        binding.root,
                        "获取天气信息失败: ${resource.message}",
                        Snackbar.LENGTH_LONG,
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.weatherForecast.onEach { resource ->
            when (resource) {
                is com.flightinfo.app.utils.Resource.Idle -> {
                    // Initial state, do nothing
                }
                is com.flightinfo.app.utils.Resource.Loading -> {
                    // Loading state handled by isLoading
                }
                is com.flightinfo.app.utils.Resource.Success -> {
                    resource.data?.let { weatherResponse ->
                        forecastAdapter.updateForecastList(weatherResponse.current.forecast)
                    }
                }
                is com.flightinfo.app.utils.Resource.Error -> {
                    Snackbar.make(
                        binding.root,
                        "获取天气预报失败: ${resource.message}",
                        Snackbar.LENGTH_LONG,
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.flightWeather.onEach { resource ->
            when (resource) {
                is com.flightinfo.app.utils.Resource.Idle -> {
                    // Initial state, do nothing
                }
                is com.flightinfo.app.utils.Resource.Loading -> {
                    // Loading state handled by isLoading
                }
                is com.flightinfo.app.utils.Resource.Success -> {
                    resource.data?.let { flightWeather ->
                        displayFlightWeather(flightWeather)
                    }
                }
                is com.flightinfo.app.utils.Resource.Error -> {
                    Snackbar.make(
                        binding.root,
                        "获取航班天气信息失败: ${resource.message}",
                        Snackbar.LENGTH_LONG,
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun displayCurrentWeather(weather: WeatherInfo) {
        binding.locationNameTextView.text = weather.location
        binding.temperatureTextView.text = "${weather.temperature}°C"
        binding.conditionTextView.text = weather.condition
        binding.humidityTextView.text = "湿度: ${weather.humidity}%"
        binding.windTextView.text = "风速: ${weather.windSpeed}km/h, ${weather.windDirection}"
    }

    private fun displayFlightWeather(flightWeather: com.flightinfo.app.data.model.FlightWeatherInfo) {
        binding.flightNumberTextView.text = "航班号: ${flightWeather.flightNumber}"

        binding.departureWeatherTextView.text =
            "出发地天气: ${flightWeather.departureWeather.location}, " +
            "${flightWeather.departureWeather.temperature}°C, " +
            flightWeather.departureWeather.condition

        binding.arrivalWeatherTextView.text =
            "目的地天气: ${flightWeather.arrivalWeather.location}, " +
            "${flightWeather.arrivalWeather.temperature}°C, " +
            flightWeather.arrivalWeather.condition

        binding.delayProbabilityTextView.text =
            "延误概率: ${(flightWeather.weatherImpact.delayProbability * 100).toInt()}%"

        binding.recommendationTextView.text =
            "建议: ${flightWeather.weatherImpact.recommendation}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
