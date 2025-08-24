package com.flightinfo.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import com.flightinfo.app.R
import com.flightinfo.app.data.model.Seat
import com.flightinfo.app.data.model.SeatClass
import com.flightinfo.app.data.model.SeatStatus
import com.flightinfo.app.ui.adapter.SeatAdapter

class SeatSelectionDialog(
    context: Context,
    private val onSeatSelectedListener: (Seat) -> Unit,
) : Dialog(context) {

    private lateinit var seatAdapter: SeatAdapter
    private lateinit var seats: List<Seat>
    private var selectedSeat: Seat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_seat_selection)

        // 初始化座位数据
        initializeSeats()

        // 设置UI组件
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val seatGridView = findViewById<GridView>(R.id.seatGridView)
        val selectedSeatTextView = findViewById<TextView>(R.id.selectedSeatTextView)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        // 设置标题
        titleTextView.text = "选择座位"

        // 初始化座位适配器
        seatAdapter = SeatAdapter(context, seats) { seat ->
            handleSeatClick(seat, selectedSeatTextView)
        }
        seatGridView.adapter = seatAdapter

        // 设置按钮点击事件
        cancelButton.setOnClickListener {
            dismiss()
        }

        confirmButton.setOnClickListener {
            selectedSeat?.let {
                onSeatSelectedListener(it)
                dismiss()
            }
        }
    }

    private fun initializeSeats() {
        seats = mutableListOf<Seat>().apply {
            // 模拟生成座位图，6列
            var id = 1
            for (row in 1..10) {
                for (col in 'A'..'F') {
                    val seatNumber = "$row$col"
                    val seatClass = when (row) {
                        in 1..2 -> SeatClass.FIRST
                        in 3..5 -> SeatClass.BUSINESS
                        in 6..8 -> SeatClass.PREMIUM
                        else -> SeatClass.ECONOMY
                    }
                    val price = when (seatClass) {
                        SeatClass.FIRST -> 1000.0
                        SeatClass.BUSINESS -> 500.0
                        SeatClass.PREMIUM -> 300.0
                        SeatClass.ECONOMY -> 100.0
                    }

                    // 随机设置一些座位为已占用
                    val status = if (Math.random() < 0.3) SeatStatus.OCCUPIED else SeatStatus.AVAILABLE

                    add(Seat(id.toString(), row, col - 'A' + 1, seatNumber, status, seatClass, price))
                    id++
                }
            }
        }
    }

    private fun handleSeatClick(seat: Seat, selectedSeatTextView: TextView) {
        if (seat.status == SeatStatus.OCCUPIED) {
            return
        }

        // 清除之前的选择
        seatAdapter.clearSelection()

        // 设置新选择的座位
        seat.status = SeatStatus.SELECTED
        selectedSeat = seat
        seatAdapter.updateSeat(seat.id, SeatStatus.SELECTED)

        // 更新选择的座位文本
        selectedSeatTextView.text = "已选座位: ${seat.seatNumber} (${getSeatClassName(seat.seatClass)})"
    }

    private fun getSeatClassName(seatClass: SeatClass): String {
        return when (seatClass) {
            SeatClass.ECONOMY -> "经济舱"
            SeatClass.PREMIUM -> "高级经济舱"
            SeatClass.BUSINESS -> "商务舱"
            SeatClass.FIRST -> "头等舱"
        }
    }
}
