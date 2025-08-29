package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flightinfo.app.R
import com.flightinfo.app.databinding.FragmentRouteMapBinding
import com.flightinfo.app.model.Airport
import com.flightinfo.app.ui.viewmodel.AirportViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.flightinfo.app.ui.viewmodel.RouteMapViewModel
import com.google.android.gms.maps.model.PolylineOptions

@AndroidEntryPoint
class RouteMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRouteMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null

    private val airportViewModel: AirportViewModel by viewModels()
    private val routeMapViewModel: RouteMapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteMapBinding.inflate(inflater, container, false)
        
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setupMap()
    }

    private fun setupMap() {
        airportViewModel.airports.observe(viewLifecycleOwner) { airports ->
            if (airports.isNotEmpty()) {
                val airportMap = airports.associateBy { it.code }
                airports.forEach { airport ->
                    val position = LatLng(airport.latitude, airport.longitude)
                    googleMap?.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title("${airport.name} (${airport.code})")
                    )
                }

                routeMapViewModel.allSchedules.observe(viewLifecycleOwner) { schedules ->
                    schedules.forEach { schedule ->
                        val departureAirport = airportMap[schedule.departureAirportCode]
                        val arrivalAirport = airportMap[schedule.arrivalAirportCode]

                        if (departureAirport != null && arrivalAirport != null) {
                            googleMap?.addPolyline(
                                PolylineOptions()
                                    .add(LatLng(departureAirport.latitude, departureAirport.longitude), LatLng(arrivalAirport.latitude, arrivalAirport.longitude))
                                    .width(2f)
                                    .color(R.color.purple_200)
                            )
                        }
                    }
                }

                // Move camera to the first airport
                val firstAirport = airports.first()
                val initialPosition = LatLng(firstAirport.latitude, firstAirport.longitude)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 5f))
            }
        }
        airportViewModel.loadAirports()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
