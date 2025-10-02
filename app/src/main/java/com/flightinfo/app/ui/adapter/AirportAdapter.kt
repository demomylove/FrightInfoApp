package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.model.Airport

class AirportAdapter(
    private val onAirportClick: (Airport) -> Unit,
) : ListAdapter<Airport, AirportAdapter.AirportViewHolder>(AirportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_airport, parent, false)
        return AirportViewHolder(view)
    }

    override fun onBindViewHolder(holder: AirportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AirportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val code: TextView = view.findViewById(R.id.textViewAirportCode)
        private val name: TextView = view.findViewById(R.id.textViewAirportName)
        private val location: TextView = view.findViewById(R.id.textViewAirportLocation)
        private val country: TextView = view.findViewById(R.id.textViewAirportCountry)

        fun bind(airport: Airport) {
            code.text = airport.code
            name.text = airport.name
            location.text = airport.city
            country.text = airport.country

            itemView.setOnClickListener {
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
