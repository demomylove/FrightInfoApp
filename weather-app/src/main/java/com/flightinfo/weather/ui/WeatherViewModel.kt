package com.flightinfo.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.weather.data.*
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val query: String = "", val searching: Boolean = false, val cities: List<City> = emptyList(),
    val searchMessage: String = "请输入城市名称", val searchFailed: Boolean = false,
    val selectedCity: City? = null, val loading: Boolean = false,
    val snapshot: WeatherSnapshot? = null, val stale: Boolean = false, val weatherError: String? = null,
)

class WeatherViewModel(private val repository: WeatherRepository, private val clock: Clock) : ViewModel() {
    private val mutable = MutableStateFlow(WeatherUiState())
    val state = mutable.asStateFlow()
    private var searchGeneration = 0L
    private var weatherGeneration = 0L
    private var searchJob: Job? = null
    private var weatherJob: Job? = null
    private var midnightJob: Job? = null
    private var requestDate: LocalDate? = null

    fun search(query: String) {
        val generation = ++searchGeneration
        searchJob?.cancel()
        val text = query.trim()
        mutable.value = mutable.value.copy(query = text, cities = emptyList(), searchFailed = false,
            searching = text.isNotEmpty(), searchMessage = if (text.isEmpty()) "请输入城市名称" else "正在搜索城市…")
        if (text.isEmpty()) return
        searchJob = viewModelScope.launch {
            try {
                val result = repository.search(text)
                if (generation == searchGeneration) mutable.value = mutable.value.copy(cities = result,
                    searchMessage = if (result.isEmpty()) "未找到城市，请修改名称" else "请选择地区；可输入更完整名称缩小范围")
            } catch (e: CancellationException) { throw e
            } catch (e: WeatherFailure) {
                if (generation == searchGeneration) mutable.value = mutable.value.copy(searchFailed = true, searchMessage = e.userMessage)
            } finally {
                if (generation == searchGeneration) mutable.value = mutable.value.copy(searching = false)
            }
        }
    }

    fun select(city: City) {
        if (mutable.value.selectedCity?.id == city.id && mutable.value.loading) return
        val sameCity = mutable.value.selectedCity?.id == city.id
        mutable.value = mutable.value.copy(selectedCity = city, snapshot = if (sameCity) mutable.value.snapshot else null,
            stale = false, weatherError = null)
        load(city)
    }

    fun refresh() {
        if (mutable.value.loading) return
        mutable.value.selectedCity?.let(::load)
    }

    private fun load(city: City) {
        val generation = ++weatherGeneration
        weatherJob?.cancel()
        val startedOn = today(clock)
        requestDate = startedOn
        mutable.value = mutable.value.copy(loading = true, weatherError = null)
        weatherJob = viewModelScope.launch {
            try {
                val snapshot = repository.load(city)
                if (!owns(generation, city)) return@launch
                if (today(clock) != startedOn) { checkDate(); return@launch }
                mutable.value = mutable.value.copy(snapshot = snapshot, stale = false, weatherError = null)
            } catch (e: CancellationException) { throw e
            } catch (e: WeatherFailure) {
                if (owns(generation, city)) {
                    if (today(clock) != startedOn) { checkDate(); return@launch }
                    mutable.value = mutable.value.copy(weatherError = e.userMessage, stale = mutable.value.snapshot != null)
                }
            } finally {
                if (owns(generation, city)) mutable.value = mutable.value.copy(loading = false)
            }
        }
    }

    private fun owns(generation: Long, city: City) = generation == weatherGeneration && city.id == mutable.value.selectedCity?.id

    fun checkDate() {
        val city = mutable.value.selectedCity ?: return
        val date = today(clock)
        if (requestDate == date) return
        mutable.value = mutable.value.copy(snapshot = mutable.value.snapshot?.forDate(date), stale = true)
        load(city)
    }

    fun onForeground() {
        checkDate()
        midnightJob?.cancel()
        midnightJob = viewModelScope.launch {
            while (true) {
                val next = today(clock).plusDays(1).atStartOfDay(BEIJING).toInstant()
                delay(Duration.between(clock.instant(), next).toMillis().coerceAtLeast(1))
                checkDate()
            }
        }
    }

    fun onBackground() { midnightJob?.cancel(); midnightJob = null }
}
