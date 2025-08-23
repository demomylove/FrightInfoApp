package com.flightinfo.app.data.model

data class SeatMapResponse(
    val flightId: String = "",
    val seats: List<Seat> = emptyList(),
    val seatClasses: List<SeatClassInfo> = emptyList(),
)

data class SeatClassInfo(
    val seatClass: SeatClass = SeatClass.ECONOMY,
    val price: Double = 0.0,
    val description: String = "",
)
