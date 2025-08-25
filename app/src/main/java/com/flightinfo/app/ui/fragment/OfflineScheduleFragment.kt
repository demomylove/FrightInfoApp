package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.flightinfo.app.R
import com.flightinfo.app.databinding.FragmentOfflineScheduleBinding
import com.flightinfo.app.ui.adapter.OfflineScheduleAdapter
import com.flightinfo.app.ui.viewmodel.OfflineScheduleUiState
import com.flightinfo.app.ui.viewmodel.OfflineScheduleViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OfflineScheduleFragment : Fragment() {

    private var _binding: FragmentOfflineScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OfflineScheduleViewModel by viewModels()
    private lateinit var scheduleAdapter: OfflineScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOfflineScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        setupFilters()
        setupClickListeners()

        observeViewModel()
    }

    private fun setupRecyclerView() {
        scheduleAdapter = OfflineScheduleAdapter()
        binding.recyclerViewSchedules.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.setSearchQuery(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })
    }

    private fun setupFilters() {
        // Day of week chips
        val days = listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
        binding.chipGroupDaysOfWeek.removeAllViews()

        days.forEachIndexed { index, day ->
            val chip = Chip(requireContext()).apply {
                text = day
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.setDayOfWeek(if (isChecked) index + 1 else null)
                }
            }
            binding.chipGroupDaysOfWeek.addView(chip)
        }

        // Setup airport spinners
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state is OfflineScheduleUiState.Success) {
                    setupAirportSpinners(state.airports)
                    setupAirlineSpinner(state.airlines)
                }
            }
        }
    }

    private fun setupAirportSpinners(airports: List<com.flightinfo.app.data.model.OfflineAirportInfo>) {
        val airportNames = airports.map { "${it.iataCode} - ${it.name}" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, airportNames)

        binding.autoCompleteDeparture.setAdapter(adapter)
        binding.autoCompleteArrival.setAdapter(adapter)

        binding.autoCompleteDeparture.setOnItemClickListener { _, _, position, _ ->
            val selectedAirport = airports[position]
            viewModel.setDepartureAirport(selectedAirport.iataCode)
        }

        binding.autoCompleteArrival.setOnItemClickListener { _, _, position, _ ->
            val selectedAirport = airports[position]
            viewModel.setArrivalAirport(selectedAirport.iataCode)
        }
    }

    private fun setupAirlineSpinner(airlines: List<com.flightinfo.app.data.model.AirlineInfo>) {
        val airlineNames = airlines.map { "${it.code} - ${it.name}" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, airlineNames)

        binding.autoCompleteAirline.setAdapter(adapter)
        binding.autoCompleteAirline.setOnItemClickListener { _, _, position, _ ->
            val selectedAirline = airlines[position]
            viewModel.setAirline(selectedAirline.name)
        }
    }

    private fun setupClickListeners() {
        binding.buttonDownload.setOnClickListener {
            viewModel.downloadSchedules()
        }

        binding.buttonClearCache.setOnClickListener {
            viewModel.clearCachedData()
        }

        binding.buttonRefresh.setOnClickListener {
            viewModel.refreshData()
        }

        binding.buttonClearFilters.setOnClickListener {
            viewModel.clearFilters()
            binding.chipGroupDaysOfWeek.clearCheck()
            binding.autoCompleteDeparture.text?.clear()
            binding.autoCompleteArrival.text?.clear()
            binding.autoCompleteAirline.text?.clear()
        }

        binding.switchAutoRefresh.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleAutoRefresh(isChecked)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUi(state)
            }
        }
    }

    private fun updateUi(state: OfflineScheduleUiState) {
        when (state) {
            is OfflineScheduleUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerViewSchedules.visibility = View.GONE
                binding.layoutNoData.visibility = View.GONE
                binding.layoutError.visibility = View.GONE
                binding.layoutFilters.visibility = View.GONE
            }

            is OfflineScheduleUiState.NoData -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewSchedules.visibility = View.GONE
                binding.layoutNoData.visibility = View.VISIBLE
                binding.layoutError.visibility = View.GONE
                binding.layoutFilters.visibility = View.GONE
            }

            is OfflineScheduleUiState.Downloading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerViewSchedules.visibility = View.GONE
                binding.layoutNoData.visibility = View.GONE
                binding.layoutError.visibility = View.GONE
                binding.layoutFilters.visibility = View.GONE
                Toast.makeText(requireContext(), "正在下载航班时刻表...", Toast.LENGTH_SHORT).show()
            }

            is OfflineScheduleUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.layoutNoData.visibility = View.GONE
                binding.layoutError.visibility = View.GONE
                binding.layoutFilters.visibility = View.VISIBLE
                binding.recyclerViewSchedules.visibility = View.VISIBLE

                scheduleAdapter.submitList(state.schedules)

                // Update download stats
                state.downloadStats?.let { stats ->
                    binding.textLastUpdate.text = if (stats.lastDownloadTime > 0) {
                        "最后更新: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                            .format(java.util.Date(stats.lastDownloadTime))}"
                    } else {
                        "暂无数据"
                    }
                    binding.textScheduleCount.text = "航班数量: ${stats.scheduleCount}"
                    binding.switchAutoRefresh.isChecked = stats.isAutoRefreshEnabled
                }
            }

            is OfflineScheduleUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewSchedules.visibility = View.GONE
                binding.layoutNoData.visibility = View.GONE
                binding.layoutError.visibility = View.VISIBLE
                binding.layoutFilters.visibility = View.GONE
                binding.textErrorMessage.text = state.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
