package com.flightinfo.app.data.model

enum class SeatStatus {
    AVAILABLE, // 可选
    SELECTED, // 已选
    OCCUPIED, // 已占
}

data class Seat(
    val id: String,
    val row: Int,
    val column: Int,
    val seatNumber: String,
    var status: SeatStatus = SeatStatus.AVAILABLE,
    val seatClass: SeatClass = SeatClass.ECONOMY,
    val price: Double = 0.0,
)

enum class SeatClass {
    ECONOMY, // 经济舱
    PREMIUM, // 高级经济舱
    BUSINESS, // 商务舱
    FIRST, // 头等舱
}
