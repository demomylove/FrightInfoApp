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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AirportLookupFragment : Fragment() {

    private var _binding: FragmentAirportLookupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AirportViewModel by viewModels()
    private lateinit var airportAdapter: AirportAdapter

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
                updateEmptyState(airports.isEmpty())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.recyclerViewAirports.visibility = if (isLoading) View.GONE else View.VISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedAirport.collect { airport ->
                airport?.let {
                    showSelectedAirport(it)
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
            // TODO: Handle airport selection (e.g., return to previous screen)
        }

        binding.buttonClearSelection.setOnClickListener {
            viewModel.clearSelection()
            binding.cardSelectedAirport.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
