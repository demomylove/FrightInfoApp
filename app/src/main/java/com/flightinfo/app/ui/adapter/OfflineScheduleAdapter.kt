package com.flightinfo.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.databinding.ItemOfflineScheduleBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OfflineScheduleAdapter : ListAdapter<FlightSchedule, OfflineScheduleAdapter.ScheduleViewHolder>(ScheduleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemOfflineScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ScheduleViewHolder(private val binding: ItemOfflineScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: FlightSchedule) {
            binding.apply {
                textFlightNumber.text = schedule.flightNumber
                textAirline.text = schedule.airline
                textRoute.text = "${schedule.departureAirportCode} → ${schedule.arrivalAirportCode}"
                textAirports.text = "${schedule.departureAirport} - ${schedule.arrivalAirport}"
                textTime.text = "${schedule.departureTime} - ${schedule.arrivalTime}"
                textDuration.text = "${schedule.duration}分钟"
                textAircraft.text = schedule.aircraftType ?: "未知机型"

                // Days of week
                val daysText = schedule.daysOfWeek.sorted().joinToString(", ") { day ->
                    when (day) {
                        1 -> "周一"
                        2 -> "周二"
                        3 -> "周三"
                        4 -> "周四"
                        5 -> "周五"
                        6 -> "周六"
                        7 -> "周日"
                        else -> ""
                    }
                }
                textDaysOfWeek.text = daysText

                // Flight type indicator
                if (schedule.isDomestic) {
                    textFlightType.text = "国内航班"
                    textFlightType.setBackgroundResource(R.drawable.status_background)
                } else {
                    textFlightType.text = "国际航班"
                    textFlightType.setBackgroundResource(R.drawable.impact_background)
                }

                // Status indicator based on current time
                val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                val status = when {
                    schedule.departureTime > currentTime -> "未起飞"
                    schedule.arrivalTime < currentTime -> "已到达"
                    else -> "飞行中"
                }
                textStatus.text = status

                // Color coding for status
                val statusColor = when (status) {
                    "未起飞" -> android.R.color.holo_blue_light
                    "已到达" -> android.R.color.holo_green_light
                    "飞行中" -> android.R.color.holo_orange_light
                    else -> android.R.color.darker_gray
                }
                textStatus.setTextColor(
                    itemView.context.getColor(statusColor),
                )
            }
        }
    }

    private class ScheduleDiffCallback : DiffUtil.ItemCallback<FlightSchedule>() {
        override fun areItemsTheSame(oldItem: FlightSchedule, newItem: FlightSchedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FlightSchedule, newItem: FlightSchedule): Boolean {
            return oldItem == newItem
        }
    }
}
