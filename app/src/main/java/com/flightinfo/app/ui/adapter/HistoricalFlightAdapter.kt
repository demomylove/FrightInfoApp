package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.HistoricalFlight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoricalFlightAdapter : ListAdapter<HistoricalFlight, HistoricalFlightAdapter.ViewHolder>(HistoricalFlightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historical_flight, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight = getItem(position)
        holder.bind(flight)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flightNumberTextView: TextView = itemView.findViewById(R.id.flightNumberTextView)
        private val routeTextView: TextView = itemView.findViewById(R.id.routeTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        fun bind(flight: HistoricalFlight) {
            flightNumberTextView.text = flight.flightNumber
            routeTextView.text = "${flight.departureAirport} -> ${flight.arrivalAirport}"
            timestampTextView.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(flight.accessedTimestamp))
        }
    }
}

class HistoricalFlightDiffCallback : DiffUtil.ItemCallback<HistoricalFlight>() {
    override fun areItemsTheSame(oldItem: HistoricalFlight, newItem: HistoricalFlight): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HistoricalFlight, newItem: HistoricalFlight): Boolean {
        return oldItem == newItem
    }
}
