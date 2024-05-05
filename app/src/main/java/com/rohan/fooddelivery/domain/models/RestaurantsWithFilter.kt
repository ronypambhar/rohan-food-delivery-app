package com.rohan.fooddelivery.domain.models

sealed interface RestaurantsWithFilter {
    data class Data(
        val filters: List<FilterInfo> = listOf(),
        val restaurants: List<RestaurantInfo> = listOf()
    ) : RestaurantsWithFilter

    data object NoDataFound : RestaurantsWithFilter
    data object UnableToReachServer : RestaurantsWithFilter
}