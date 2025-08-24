package com.flightinfo.app.data.model

data class PriceRangeFilter(
    val minPrice: Float,
    val maxPrice: Float,
    val isEnabled: Boolean = true,
) {
    fun isPriceInRange(price: Double): Boolean {
        return if (!isEnabled) {
            true
        } else {
            price >= minPrice && price <= maxPrice
        }
    }
}
