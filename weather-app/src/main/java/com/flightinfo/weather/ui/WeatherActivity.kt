package com.flightinfo.weather.ui

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flightinfo.weather.R
import com.flightinfo.weather.WeatherApplication
import com.flightinfo.weather.data.*
import com.flightinfo.weather.databinding.ActivityWeatherBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.launch

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val model: WeatherViewModel by viewModels {
        val container = (application as WeatherApplication).container
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                WeatherViewModel(container.repository, container.clock) as T
        }
    }
    private var renderedCities: List<City>? = null
    private val decimal = DecimalFormat("0.#", DecimalFormatSymbols(Locale.CHINA))
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy年M月d日 EEEE", Locale.CHINA)
    private val timeFormat = DateTimeFormatter.ofPattern("MM-dd HH:mm", Locale.CHINA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.search.setOnClickListener { model.search(binding.query.text.toString()) }
        binding.query.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) { model.search(binding.query.text.toString()); true } else false
        }
        binding.searchRetry.setOnClickListener { model.search(model.state.value.query) }
        binding.refresh.setOnClickListener { model.refresh() }
        binding.sources.text = HtmlCompat.fromHtml(getString(R.string.sources), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.sources.movementMethod = LinkMovementMethod.getInstance()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { model.state.collect(::render) }
        }
    }

    override fun onStart() { super.onStart(); model.onForeground() }
    override fun onStop() { model.onBackground(); super.onStop() }

    private fun temperature(value: Double?) = value?.let { "${decimal.format(it)}℃" } ?: "暂无数据"

    private fun render(state: WeatherUiState) {
        binding.searchStatus.text = state.searchMessage
        binding.searchRetry.isVisible = state.searchFailed
        if (renderedCities != state.cities) {
            renderedCities = state.cities
            binding.cities.removeAllViews()
            state.cities.forEach { city ->
                binding.cities.addView(Button(this).apply {
                    text = city.label
                    isAllCaps = false
                    setOnClickListener { model.select(city) }
                })
            }
        }
        binding.city.text = state.selectedCity?.label ?: getString(R.string.empty_city)
        binding.refresh.isEnabled = state.selectedCity != null && !state.loading
        binding.refresh.text = if (state.weatherError != null) "重试天气" else getString(R.string.refresh)
        binding.loading.isVisible = state.loading
        val snapshot = state.snapshot
        binding.weatherStatus.text = when {
            state.loading && snapshot != null -> "正在更新，暂显示上次数据"
            state.loading -> "正在获取天气…"
            state.weatherError != null && snapshot != null -> "更新失败，显示上次数据\n${state.weatherError}"
            state.weatherError != null -> state.weatherError
            state.stale -> "日期已变化，数据尚未更新，请刷新"
            snapshot != null && !snapshot.complete -> "天气数据不完整，请刷新重试"
            snapshot != null -> "已更新"
            else -> getString(R.string.initial)
        }
        binding.current.text = snapshot?.let { "${temperature(it.temperature)} · ${weatherText(it.code)}" } ?: ""
        binding.times.text = snapshot?.let {
            "数据时间（北京时间）：${it.dataTime?.format(timeFormat) ?: "不可用"}\n" +
                "本次获取时间（北京时间）：${it.receivedAt.atZone(BEIJING).format(timeFormat)}"
        } ?: ""
        binding.forecast.removeAllViews()
        snapshot?.days?.forEach { day ->
            binding.forecast.addView(TextView(this).apply {
                text = "${day.date.format(dateFormat)}\n${weatherText(day.code)}    最高 ${temperature(day.high)} / 最低 ${temperature(day.low)}"
                textSize = 16f
                setPadding(0, 20, 0, 20)
            })
        }
        if (snapshot != null && snapshot.days.size != 7) {
            binding.forecast.addView(TextView(this).apply { text = "预报日期不足，请刷新" })
        }
    }
}
