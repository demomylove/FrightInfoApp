package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.databinding.ItemAirportBinding
import com.flightinfo.app.model.Airport

class AirportAdapter(
    private val onAirportClick: (Airport) -> Unit,
) : ListAdapter<Airport, AirportAdapter.AirportViewHolder>(AirportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportViewHolder {
        val binding = ItemAirportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return AirportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AirportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AirportViewHolder(
        private val binding: ItemAirportBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(airport: Airport) {
            binding.textViewAirportCode.text = airport.code
            binding.textViewAirportName.text = airport.name
            binding.textViewAirportLocation.text = airport.city
            binding.textViewAirportCountry.text = airport.country

            binding.root.setOnClickListener {
                onAirportClick(airport)
            }
        }
    }
}

private class AirportDiffCallback : DiffUtil.ItemCallback<Airport>() {
    override fun areItemsTheSame(oldItem: Airport, newItem: Airport): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Airport, newItem: Airport): Boolean {
        return oldItem == newItem
    }
}
