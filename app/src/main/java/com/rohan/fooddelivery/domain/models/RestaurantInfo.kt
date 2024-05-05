package com.rohan.fooddelivery.domain.models

import java.io.Serializable

data class RestaurantInfo(
    val restaurantId: String = "",
    val restaurantName: String = "",
    val imageUrl: String = "",
    val deliveryTimeMinutes: Int = 0,
    val rating: Float = 0f,
    val filterIds: List<String> = listOf()
) : Serializable {

    var filterNames: List<String> = listOf()

    fun generateFilterName() = filterNames.joinToString(" • ")
}