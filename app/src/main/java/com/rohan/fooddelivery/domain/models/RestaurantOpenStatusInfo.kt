package com.rohan.fooddelivery.domain.models

sealed interface RestaurantOpenStatusInfo {
    data class Data(
        val data: OpenStatusInfo
    ) : RestaurantOpenStatusInfo

    data object NoStatusFound : RestaurantOpenStatusInfo
}
