package com.flightinfo.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import com.flightinfo.app.R
import com.flightinfo.app.data.model.Seat
import com.flightinfo.app.data.model.SeatStatus

class SeatAdapter(
    context: Context,
    private val seats: List<Seat>,
    private val onSeatClickListener: (Seat) -> Unit,
) : ArrayAdapter<Seat>(context, R.layout.item_seat, seats) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false)
        val seatButton = view.findViewById<Button>(R.id.seatButton)
        val seat = getItem(position)

        seat?.let {
            seatButton.text = it.seatNumber
            seatButton.isSelected = it.status == SeatStatus.SELECTED
            seatButton.isEnabled = it.status != SeatStatus.OCCUPIED

            seatButton.setOnClickListener {
                onSeatClickListener(seat)
            }
        }

        return view
    }

    fun updateSeat(seatId: String, status: SeatStatus) {
        seats.find { it.id == seatId }?.status = status
        notifyDataSetChanged()
    }

    fun getSelectedSeat(): Seat? {
        return seats.find { it.status == SeatStatus.SELECTED }
    }

    fun clearSelection() {
        seats.forEach { if (it.status == SeatStatus.SELECTED) it.status = SeatStatus.AVAILABLE }
        notifyDataSetChanged()
    }
}
