package com.rohan.fooddelivery.data.network

import com.rohan.fooddelivery.data.models.FilterResponse
import com.rohan.fooddelivery.data.models.OpenStatusResponse
import com.rohan.fooddelivery.data.models.RestaurantsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface IFoodDeliveryApi {
    @GET("v1/filter/{id}")
    suspend fun getFilterById(@Path("id") filterId: String): FilterResponse

    @GET("v1/restaurants")
    suspend fun getRestaurants(): RestaurantsResponse

    @GET("v1/open/{id}")
    suspend fun getOpenStatusByRestaurantId(@Path("id") restaurantId: String): OpenStatusResponse
}