package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.data.model.DailyForecast
import com.flightinfo.app.databinding.ItemForecastBinding

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private val forecastList = mutableListOf<DailyForecast>()

    fun updateForecastList(newList: List<DailyForecast>) {
        forecastList.clear()
        forecastList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount(): Int = forecastList.size

    inner class ForecastViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: DailyForecast) {
            binding.dateTextView.text = forecast.date
            binding.conditionTextView.text = forecast.condition
            binding.maxTempTextView.text = "${forecast.maxTemp}°C"
            binding.minTempTextView.text = "${forecast.minTemp}°C"
            binding.precipitationTextView.text = "降水: ${forecast.precipitation}%"
            binding.windTextView.text = "风速: ${forecast.windSpeed}km/h"
        }
    }
}
