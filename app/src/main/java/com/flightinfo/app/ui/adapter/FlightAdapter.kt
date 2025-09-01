package com.flightinfo.app.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.EnvironmentalImpact
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.databinding.ItemFlightBinding
import java.text.SimpleDateFormat
import java.util.Locale

class FlightAdapter(
    private val onFlightClick: (FlightInfo) -> Unit = {},
    private val onBookClick: (FlightInfo) -> Unit = {},
    private val onTrackClick: (FlightInfo, Boolean) -> Unit = { _, _ -> },
    private val onBookmarkClick: (FlightInfo) -> Unit = {},
    private val onFavoriteRouteClick: (FlightInfo) -> Unit = {},
    private val onShareClick: (FlightInfo) -> Unit = {},
) : ListAdapter<FlightInfo, FlightAdapter.FlightViewHolder>(FlightDiffCallback()) {

    private val bookmarkedFlightNumbers = mutableSetOf<String>()
    private val favoriteRoutePairs = mutableSetOf<Pair<String, String>>()

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

    fun setBookmarkedFlights(bookmarkedFlights: List<String>) {
        bookmarkedFlightNumbers.clear()
        bookmarkedFlightNumbers.addAll(bookmarkedFlights)
        notifyDataSetChanged()
    }

    fun setFavoriteRoutes(favoriteRoutes: List<Pair<String, String>>) {
        favoriteRoutePairs.clear()
        favoriteRoutePairs.addAll(favoriteRoutes)
        notifyDataSetChanged()
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

            binding.trackButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val flight = getItem(position)
                    // For now, we'll just toggle tracking
                    // In a real implementation, we'd check if the flight is already tracked
                    onTrackClick(flight, true)
                }
            }

            binding.bookmarkIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBookmarkClick(getItem(position))
                }
            }

            binding.favoriteRouteIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFavoriteRouteClick(getItem(position))
                }
            }

            binding.shareIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val flight = getItem(position)
                    val shareText = """
                        Check out this flight:
                        Flight: ${flight.flightNumber} (${flight.airline})
                        From: ${flight.departureAirport} at ${formatTime(flight.departureTime)}
                        To: ${flight.arrivalAirport} at ${formatTime(flight.arrivalTime)}
                        Status: ${flight.status.uppercase()}
                    """.trimIndent()

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Flight Information: ${flight.flightNumber}")
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    binding.root.context.startActivity(Intent.createChooser(intent, "Share Flight via"))
                    onShareClick(flight)
                }
            }
        }

        fun bind(flight: FlightInfo) {
            binding.flightNumberText.text = flight.flightNumber
            binding.airlineText.text = flight.airline

            // Format and display times
            binding.departureTimeText.text = formatTime(flight.departureTime)
            binding.arrivalTimeText.text = formatTime(flight.arrivalTime)

            binding.departureAirportText.text = flight.departureAirport
            binding.arrivalAirportText.text = flight.arrivalAirport

            // Price display
            flight.price?.let { price ->
                val currency = flight.currency ?: "USD"
                val formattedPrice = when (currency) {
                    "USD" -> String.format("$%.2f", price)
                    "EUR" -> String.format("€%.2f", price)
                    "GBP" -> String.format("£%.2f", price)
                    "CNY" -> String.format("¥%.2f", price)
                    else -> String.format("%.2f %s", price, currency)
                }
                binding.priceText.text = formattedPrice
                binding.priceText.visibility = View.VISIBLE
            } ?: run {
                binding.priceText.visibility = View.GONE
            }

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

            // Carbon footprint
            flight.carbonFootprint?.let { carbon ->
                binding.carbonFootprintLayout.visibility = View.VISIBLE
                binding.carbonValueText.text = String.format("%.1f kg CO2", carbon.co2EmissionKg)
                binding.carbonImpactText.text = getCarbonImpactText(carbon.getEnvironmentalImpact())
            } ?: run {
                binding.carbonFootprintLayout.visibility = View.GONE
            }

            // Set bookmark icon state
            if (bookmarkedFlightNumbers.contains(flight.flightNumber)) {
                binding.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_filled)
            } else {
                binding.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border)
            }

            // Set favorite route icon state
            if (favoriteRoutePairs.contains(Pair(flight.departureAirport, flight.arrivalAirport))) {
                binding.favoriteRouteIcon.setImageResource(R.drawable.ic_star_filled)
            } else {
                binding.favoriteRouteIcon.setImageResource(R.drawable.ic_star_border)
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

        private fun getCarbonImpactText(impact: EnvironmentalImpact): String {
            return when (impact) {
                EnvironmentalImpact.LOW -> "Low impact - Environmentally friendly"
                EnvironmentalImpact.MEDIUM -> "Medium impact - Consider carbon offset"
                EnvironmentalImpact.HIGH -> "High impact - Consider alternative transport"
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
