package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.flightinfo.app.R
import com.flightinfo.app.data.model.CarbonFootprintStats
import com.flightinfo.app.data.model.MonthlyEmission
import com.flightinfo.app.databinding.FragmentCarbonFootprintStatsBinding
import com.flightinfo.app.ui.adapter.CarbonStatsAdapter
import com.flightinfo.app.ui.viewmodel.FlightSearchViewModel

class CarbonFootprintStatsFragment : Fragment() {

    private var _binding: FragmentCarbonFootprintStatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlightSearchViewModel by activityViewModels()
    private lateinit var statsAdapter: CarbonStatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCarbonFootprintStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeCarbonStats()
        setupRefresh()
    }

    private fun setupRecyclerView() {
        statsAdapter = CarbonStatsAdapter()
        binding.statsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = statsAdapter
        }
    }

    private fun observeCarbonStats() {
        // For now, display mock data
        displayMockStats()
    }

    private fun displayStats(stats: CarbonFootprintStats) {
        // Total emissions
        binding.totalEmissionsText.text = String.format("%.1f kg CO2", stats.totalEmissionsKg)
        binding.totalFlightsText.text = "${stats.flightCount} flights"
        binding.averageEmissionText.text = String.format("%.1f kg CO2/flight", stats.averageEmissionPerFlight)
        binding.totalDistanceText.text = String.format("%.0f km", stats.totalDistanceKm)

        // Environmental impact assessment
        val impactLevel = when {
            stats.totalEmissionsKg < 1000 -> "Low"
            stats.totalEmissionsKg < 5000 -> "Medium"
            else -> "High"
        }
        binding.impactLevelText.text = impactLevel
        binding.impactLevelText.setTextColor(getImpactColor(impactLevel))

        // Monthly trend
        statsAdapter.submitList(stats.monthlyTrend)

        // Airline breakdown
        displayAirlineBreakdown(stats.airlineBreakdown)

        // Equivalent metrics
        val carKm = stats.totalEmissionsKg * 4.5
        val trees = (stats.totalEmissionsKg / 21.77).toInt()
        binding.equivalentCarText.text = String.format("%.0f km", carKm)
        binding.equivalentTreesText.text = "$trees trees"
    }

    private fun displayAirlineBreakdown(breakdown: Map<String, Double>) {
        val breakdownText = breakdown.entries
            .sortedByDescending { it.value }
            .take(5)
            .joinToString("\n") { entry ->
                val percentage = (entry.value / breakdown.values.sum() * 100)
                "${entry.key}: ${String.format("%.1f", percentage)}%"
            }
        binding.airlineBreakdownText.text = breakdownText
    }

    private fun getImpactColor(impact: String): Int {
        return when (impact.lowercase()) {
            "low" -> requireContext().getColor(R.color.status_on_time)
            "medium" -> requireContext().getColor(R.color.status_delayed)
            "high" -> requireContext().getColor(R.color.status_cancelled)
            else -> requireContext().getColor(R.color.primary_color)
        }
    }

    private fun setupRefresh() {
        // Swipe refresh functionality removed for now
    }

    private fun displayMockStats() {
        val mockStats = CarbonFootprintStats(
            totalEmissionsKg = 1234.5,
            totalDistanceKm = 15234.0,
            flightCount = 24,
            averageEmissionPerFlight = 51.4,
            monthlyTrend = listOf(
                MonthlyEmission("Jan", 100.0, 2),
                MonthlyEmission("Feb", 150.0, 3),
                MonthlyEmission("Mar", 120.0, 2),
                MonthlyEmission("Apr", 200.0, 4),
                MonthlyEmission("May", 180.0, 3),
                MonthlyEmission("Jun", 164.5, 3),
            ),
            airlineBreakdown = mapOf(
                "Air China" to 45.2,
                "China Eastern" to 32.1,
                "China Southern" to 22.7,
            ),
        )
        displayStats(mockStats)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
