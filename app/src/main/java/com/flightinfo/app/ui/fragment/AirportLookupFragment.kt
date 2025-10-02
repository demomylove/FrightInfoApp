package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.flightinfo.app.databinding.FragmentAirportLookupBinding
import com.flightinfo.app.model.Airport
import com.flightinfo.app.ui.adapter.AirportAdapter
import com.flightinfo.app.ui.viewmodel.AirportViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AirportLookupFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentAirportLookupBinding? = null
    val binding get() = _binding!!

    private val viewModel: AirportViewModel by viewModels()
    private lateinit var airportAdapter: AirportAdapter
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAirportLookupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchInput()
        observeViewModel()

        // Only initialize map if Google Play Services is available
        if (isGooglePlayServicesAvailable()) {
            binding.mapView.onCreate(savedInstanceState)
            binding.mapView.getMapAsync(this)
        } else {
            // Hide map view if Google Play Services is not available
            binding.mapView.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        airportAdapter = AirportAdapter { airport ->
            viewModel.selectAirport(airport)
        }

        binding.recyclerViewAirports.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = airportAdapter
        }
    }

    private fun setupSearchInput() {
        binding.editTextSearchAirport.setOnKeyListener { _, keyCode, event ->
            if (event.action == android.view.KeyEvent.ACTION_DOWN && keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                val query = binding.editTextSearchAirport.text.toString()
                viewModel.updateSearchQuery(query)
                true
            } else {
                false
            }
        }

        binding.buttonSearch.setOnClickListener {
            val query = binding.editTextSearchAirport.text.toString()
            viewModel.updateSearchQuery(query)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResults.collect { airports ->
                airportAdapter.submitList(airports)
                updateEmptyState(airports.isEmpty() && viewModel.searchQuery.value.isNotEmpty())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedAirport.collect { airport ->
                if (airport != null) {
                    showSelectedAirport(airport)
                } else {
                    hideSelectedAirport()
                }
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.textViewEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewAirports.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun showSelectedAirport(airport: Airport) {
        binding.cardSelectedAirport.visibility = View.VISIBLE
        binding.textViewSelectedAirportCode.text = airport.code
        binding.textViewSelectedAirportName.text = airport.name
        binding.textViewSelectedAirportLocation.text = "${airport.city}, ${airport.country}"

        binding.buttonUseAirport.setOnClickListener {
            // TODO: Handle airport selection
        }

        binding.buttonClearSelection.setOnClickListener {
            viewModel.clearSelection()
        }

        updateMap(airport)
    }

    private fun hideSelectedAirport() {
        binding.cardSelectedAirport.visibility = View.GONE
    }

    private fun updateMap(airport: Airport) {
        if (!isGooglePlayServicesAvailable()) {
            return
        }
        googleMap?.let { map ->
            val location = LatLng(airport.latitude, airport.longitude)
            map.clear()
            map.addMarker(MarkerOptions().position(location).title(airport.name))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
            binding.mapView.visibility = View.VISIBLE
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        // If an airport is already selected when the map is ready, show it
        viewModel.selectedAirport.value?.let {
            updateMap(it)
        }
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        return try {
            val result = com.google.android.gms.common.GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(requireContext())
            result == com.google.android.gms.common.ConnectionResult.SUCCESS
        } catch (e: Exception) {
            false
        }
    }

    override fun onResume() {
        super.onResume()
        if (isGooglePlayServicesAvailable()) {
            binding.mapView.onResume()
        }
    }

    override fun onStart() {
        super.onStart()
        if (isGooglePlayServicesAvailable()) {
            binding.mapView.onStart()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isGooglePlayServicesAvailable()) {
            binding.mapView.onStop()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isGooglePlayServicesAvailable()) {
            binding.mapView.onPause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isGooglePlayServicesAvailable()) {
            binding.mapView.onDestroy()
        }
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (isGooglePlayServicesAvailable()) {
            binding.mapView.onLowMemory()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isGooglePlayServicesAvailable()) {
            binding.mapView.onSaveInstanceState(outState)
        }
    }
}
