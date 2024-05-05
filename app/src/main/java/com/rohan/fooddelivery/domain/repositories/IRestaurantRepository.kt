package com.rohan.fooddelivery.domain.repositories

import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.models.OpenStatusInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo

interface IRestaurantRepository {

    suspend fun getFilterByID(filterId: String): FilterInfo

    suspend fun getRestaurants(): List<RestaurantInfo>

    suspend fun getOpenStatusById(restaurantId: String): OpenStatusInfo
}