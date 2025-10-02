package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.databinding.ItemMonthlyEmissionBinding

data class MonthlyEmission(
    val month: String,
    val emissionsKg: Double,
    val flights: Int,
    val progress: Int,
)

class CarbonStatsAdapter : RecyclerView.Adapter<CarbonStatsAdapter.ViewHolder>() {
    private val items = mutableListOf<MonthlyEmission>()

    fun submit(list: List<MonthlyEmission>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMonthlyEmissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemMonthlyEmissionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MonthlyEmission) {
            binding.monthText.text = item.month
            binding.emissionsText.text = String.format("%.1f kg CO2", item.emissionsKg)
            binding.flightsText.text = "${item.flights} flights"
            binding.emissionBar.progress = item.progress
        }
    }
}
