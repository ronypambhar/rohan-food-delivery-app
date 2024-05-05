package com.rohan.fooddelivery.data.models

data class RestaurantsResponse(
    val restaurants: List<RestaurantItem?>
) {
    data class RestaurantItem(
        val id: String?,
        val delivery_time_minutes: Int?,
        val filterIds: List<String>?,
        val image_url: String?,
        val name: String?,
        val rating: Float?
    )
}
