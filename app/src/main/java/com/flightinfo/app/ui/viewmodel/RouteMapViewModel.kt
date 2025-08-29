package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.data.dao.FlightScheduleDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouteMapViewModel @Inject constructor(
    private val flightScheduleDao: FlightScheduleDao
) : ViewModel() {

    val allSchedules: LiveData<List<FlightSchedule>> = flightScheduleDao.getAllSchedules().asLiveData()

}
