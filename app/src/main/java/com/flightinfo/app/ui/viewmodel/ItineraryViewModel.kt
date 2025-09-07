package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.Itinerary
import com.flightinfo.app.data.model.ItinerarySegment
import com.flightinfo.app.data.model.TripTask
import com.flightinfo.app.data.repository.ItineraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val repository: ItineraryRepository,
) : ViewModel() {

    private val _itineraries = MutableStateFlow<List<Itinerary>>(emptyList())
    val itineraries: StateFlow<List<Itinerary>> = _itineraries.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAll().collect { list ->
                _itineraries.value = list
            }
        }
    }

    fun createSampleItineraryIfEmpty(now: Long) {
        if (_itineraries.value.isNotEmpty()) return
        viewModelScope.launch {
            val segStart = now + 3 * 60 * 60 * 1000 // +3h
            val segEnd = segStart + 2 * 60 * 60 * 1000 // +2h
            val itinerary = Itinerary(
                title = "示例行程",
                pnr = "AB12CD",
                passengers = listOf("张三"),
                startTime = segStart,
                endTime = segEnd,
                segments = listOf(
                    ItinerarySegment(
                        flightNumber = "MU5123",
                        airline = "China Eastern",
                        departureAirport = "PEK",
                        arrivalAirport = "SHA",
                        departureTime = segStart,
                        arrivalTime = segEnd,
                        gate = "C12",
                        terminal = "T2",
                    ),
                ),
                tasks = listOf(
                    TripTask(id = UUID.randomUUID().toString(), title = "提前出发前往机场", dueTime = segStart - 3_600_000L),
                    TripTask(id = UUID.randomUUID().toString(), title = "值机并获取登机牌", dueTime = segStart - 2_700_000L),
                ),
                checkInUrl = "https://www.example-airline.com/checkin",
                boardingPassUrl = "https://www.example-airline.com/boardingpass",
            )
            repository.add(itinerary)
        }
    }

    fun toggleTask(itinerary: Itinerary, taskId: String) {
        viewModelScope.launch {
            val newTasks = itinerary.tasks.map { t -> if (t.id == taskId) t.copy(completed = !t.completed) else t }
            repository.update(itinerary.copy(tasks = newTasks))
        }
    }
}
