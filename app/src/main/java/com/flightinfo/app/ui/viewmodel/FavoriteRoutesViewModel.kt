package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.repository.FavoriteRouteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteRoutesViewModel @Inject constructor(
    private val repository: FavoriteRouteRepository,
) : ViewModel() {

    val favoriteRoutes = repository.getAllFavoriteRoutes().asLiveData()

    fun addFavorite(originAirport: String, destinationAirport: String) {
        viewModelScope.launch {
            repository.addFavorite(originAirport, destinationAirport)
        }
    }

    fun removeFavorite(originAirport: String, destinationAirport: String) {
        viewModelScope.launch {
            repository.removeFavorite(originAirport, destinationAirport)
        }
    }
}
