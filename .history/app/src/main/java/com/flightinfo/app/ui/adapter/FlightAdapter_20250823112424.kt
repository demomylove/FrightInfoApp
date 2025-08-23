package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.databinding.ItemFlightBinding
import java.text.SimpleDateFormat
import java.util.*

class FlightAdapter(
    private val onFlightClick: (FlightInfo) -> Unit = {},
    private val onBookClick: (FlightInfo) -> Unit = {},
    private val onTrackClick: (FlightInfo, Boolean) -> Unit = { _, _ -> },
) : ListAdapter<FlightInfo, FlightAdapter.FlightViewHolder>(FlightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val binding = ItemFlightBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return FlightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FlightViewHolder(
        private val binding: ItemFlightBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFlightClick(getItem(position))
                }
            }

            binding.bookButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBookClick(getItem(position))
                }
            }

            // Remove the trackButton click listener for now since we're getting compilation errors
        }

        fun bind(flight: FlightInfo) {
            binding.flightNumberText.text = flight.flightNumber
            binding.airlineText.text = flight.airline

            // Format and display times
            binding.departureTimeText.text = formatTime(flight.departureTime)
            binding.arrivalTimeText.text = formatTime(flight.arrivalTime)

            binding.departureAirportText.text = flight.departureAirport
            binding.arrivalAirportText.text = flight.arrivalAirport

            // Status with color
            binding.statusText.text = flight.status.uppercase()
            binding.statusText.setBackgroundColor(getStatusColor(flight.status))

            // Gate and Terminal info
            flight.gate?.let { gate ->
                binding.gateText.text = "Gate $gate"
            }

            flight.terminal?.let { terminal ->
                binding.terminalText.text = "Terminal $terminal"
            }

            // Aircraft type
            flight.aircraftType?.let { aircraft ->
                binding.aircraftText.text = aircraft
            }
        }

        private fun formatTime(timeString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = inputFormat.parse(timeString)
                date?.let { outputFormat.format(it) } ?: timeString
            } catch (e: Exception) {
                timeString
            }
        }

        private fun getStatusColor(status: String): Int {
            return when (status.lowercase()) {
                "on time" -> ContextCompat.getColor(binding.root.context, R.color.status_on_time)
                "delayed" -> ContextCompat.getColor(binding.root.context, R.color.status_delayed)
                "cancelled" -> ContextCompat.getColor(binding.root.context, R.color.status_cancelled)
                "boarding" -> ContextCompat.getColor(binding.root.context, R.color.status_boarding)
                "departed", "in flight" -> ContextCompat.getColor(binding.root.context, R.color.status_departed)
                else -> ContextCompat.getColor(binding.root.context, R.color.primary_color)
            }
        }
    }

    private class FlightDiffCallback : DiffUtil.ItemCallback<FlightInfo>() {
        override fun areItemsTheSame(oldItem: FlightInfo, newItem: FlightInfo): Boolean {
            return oldItem.flightNumber == newItem.flightNumber
        }

        override fun areContentsTheSame(oldItem: FlightInfo, newItem: FlightInfo): Boolean {
            return oldItem == newItem
        }
    }
}
