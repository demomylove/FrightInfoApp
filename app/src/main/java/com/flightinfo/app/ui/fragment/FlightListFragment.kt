package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.flightinfo.app.R
import com.flightinfo.app.data.repository.FavoriteRouteRepository
import com.flightinfo.app.data.repository.HistoricalFlightRepository
import com.flightinfo.app.databinding.FragmentFlightListBinding
import com.flightinfo.app.ui.adapter.FlightAdapter
import com.flightinfo.app.ui.dialog.PriceRangeFilterDialog
import com.flightinfo.app.ui.viewmodel.FlightSearchViewModel
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FlightListFragment : Fragment() {

    @Inject
    lateinit var favoriteRouteRepository: FavoriteRouteRepository

    @Inject
    lateinit var historicalFlightRepository: HistoricalFlightRepository

    private var _binding: FragmentFlightListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlightSearchViewModel by activityViewModels()
    private lateinit var flightAdapter: FlightAdapter

    private var isRealtime: Boolean = false

    companion object {
        private const val ARG_IS_REALTIME = "is_realtime"

        fun newInstance(isRealtime: Boolean): FlightListFragment {
            return FlightListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_REALTIME, isRealtime)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRealtime = arguments?.getBoolean(ARG_IS_REALTIME) ?: false
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFlightListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()
        observeFlights()
        observeBookmarks()
        observeFavoriteRoutes()
    }

    private fun setupRecyclerView() {
        flightAdapter = FlightAdapter(
            onFlightClick = { flight ->
                val bundle = Bundle().apply {
                    putString("destination", flight.arrivalAirport)
                }
                findNavController().navigate(R.id.action_flightListFragment_to_travelSuggestionFragment, bundle)
                lifecycleScope.launch { historicalFlightRepository.addHistoricalFlight(flight) }
            },
            onBookClick = { flight ->
                val bundle = Bundle().apply {
                    putString("flightId", flight.flightNumber)
                }
                findNavController().navigate(R.id.action_flightListFragment_to_flightBookingFragment, bundle)
            },
            onTrackClick = { flight, isTracked ->
                // Toggle tracking for the flight
                if (isTracked) {
                    viewModel.untrackFlight(flight.flightNumber)
                } else {
                    viewModel.trackFlight(flight.flightNumber, flight.flightNumber, flight.status)
                }
            },
            onBookmarkClick = { flight ->
                viewModel.toggleBookmark(flight)
            },
            onFavoriteRouteClick = { flight ->
                lifecycleScope.launch {
                    val isFavorite = favoriteRouteRepository.isFavorite(flight.departureAirport, flight.arrivalAirport).first()
                    if (isFavorite) {
                        favoriteRouteRepository.removeFavorite(flight.departureAirport, flight.arrivalAirport)
                    } else {
                        favoriteRouteRepository.addFavorite(flight.departureAirport, flight.arrivalAirport)
                    }
                }
            },
        )

        binding.flightsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = flightAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (isRealtime) {
                viewModel.refreshRealtimeFlights()
            } else {
                // For search results, we could re-run the last search
                viewModel.clearResults()
            }
        }
    }

    private fun observeFlights() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val flow = if (isRealtime) {
                    viewModel.realtimeFlights
                } else {
                    viewModel.filteredSearchResults
                }

                flow.collect { resource ->
                    binding.swipeRefreshLayout.isRefreshing = false

                    when (resource) {
                        is Resource.Idle -> {
                            // Initial state, do nothing
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                            hideError()
                            hideEmpty()
                        }

                        is Resource.Success -> {
                            showLoading(false)
                            hideError()

                            resource.data?.let { response ->
                                if (response.flights.isEmpty()) {
                                    showEmpty()
                                } else {
                                    hideEmpty()
                                    flightAdapter.submitList(response.flights)
                                }
                            }
                        }

                        is Resource.Error -> {
                            showLoading(false)
                            hideEmpty()
                            showError(resource.message ?: "Unknown error occurred")
                        }
                    }
                }
            }
        }
    }

    private fun observeBookmarks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookmarkedFlights.collect { bookmarkedFlights ->
                    flightAdapter.setBookmarkedFlights(bookmarkedFlights)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_flight_list, menu)

        // Show filter icon only for search results (not realtime flights)
        val filterItem = menu.findItem(R.id.action_filter)
        filterItem?.isVisible = !isRealtime
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showPriceRangeFilter()
                true
            }
            R.id.action_route_map -> {
                findNavController().navigate(R.id.action_flightListFragment_to_routeMapFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPriceRangeFilter() {
        val dialog = PriceRangeFilterDialog()
        dialog.show(parentFragmentManager, "PriceRangeFilterDialog")
    }

    private fun showLoading(show: Boolean) {
        binding.loadingOverlay.isVisible = show
    }

    private fun showError(message: String = "Something went wrong") {
        binding.errorStateLayout.isVisible = true
        binding.errorText.text = message

        binding.retryButton.setOnClickListener {
            if (isRealtime) {
                viewModel.refreshRealtimeFlights()
            }
        }
    }

    private fun hideError() {
        binding.errorStateLayout.isVisible = false
    }

    private fun showEmpty() {
        binding.emptyStateLayout.isVisible = true
        if (isRealtime) {
            binding.emptyStateText.text = "No real-time flights available"
            binding.emptyStateSubtext.text = "Pull to refresh for latest data"
        }
    }

    private fun hideEmpty() {
        binding.emptyStateLayout.isVisible = false
    }

    private fun observeFavoriteRoutes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteRouteRepository.getAllFavoriteRoutes().collect {
                    val favoritePairs = it.map { route -> Pair(route.originAirport, route.destinationAirport) }
                    flightAdapter.setFavoriteRoutes(favoritePairs)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
