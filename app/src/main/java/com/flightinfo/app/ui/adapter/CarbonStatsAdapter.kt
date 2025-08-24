package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.data.model.MonthlyEmission
import com.flightinfo.app.databinding.ItemMonthlyEmissionBinding

class CarbonStatsAdapter : ListAdapter<MonthlyEmission, CarbonStatsAdapter.MonthlyEmissionViewHolder>(MonthlyEmissionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyEmissionViewHolder {
        val binding = ItemMonthlyEmissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MonthlyEmissionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthlyEmissionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MonthlyEmissionViewHolder(
        private val binding: ItemMonthlyEmissionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(monthlyEmission: MonthlyEmission) {
            binding.monthText.text = monthlyEmission.month
            binding.emissionsText.text = String.format("%.1f kg CO2", monthlyEmission.emissionsKg)
            binding.flightsText.text = "${monthlyEmission.flightCount} flights"

            // Simple bar visualization
            val maxEmission = 500.0 // Adjust based on your data
            val progress = (monthlyEmission.emissionsKg / maxEmission).coerceAtMost(1.0)
            binding.emissionBar.progress = (progress * 100).toInt()
        }
    }

    private class MonthlyEmissionDiffCallback : DiffUtil.ItemCallback<MonthlyEmission>() {
        override fun areItemsTheSame(oldItem: MonthlyEmission, newItem: MonthlyEmission): Boolean {
            return oldItem.month == newItem.month
        }

        override fun areContentsTheSame(oldItem: MonthlyEmission, newItem: MonthlyEmission): Boolean {
            return oldItem == newItem
        }
    }
}
