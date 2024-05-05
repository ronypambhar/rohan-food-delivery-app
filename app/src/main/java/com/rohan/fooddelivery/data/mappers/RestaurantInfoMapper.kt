package com.rohan.fooddelivery.data.mappers

import com.rohan.fooddelivery.data.models.RestaurantsResponse
import com.rohan.fooddelivery.domain.models.RestaurantInfo

class RestaurantInfoMapper : Mapper<RestaurantsResponse, List<RestaurantInfo>> {

    override fun map(input: RestaurantsResponse): List<RestaurantInfo> {
        return input.restaurants
            .filterNotNull()
            .map { item ->
                RestaurantInfo(
                    restaurantId = item.id.orEmpty(),
                    restaurantName = item.name.orEmpty(),
                    imageUrl = item.image_url.orEmpty(),
                    deliveryTimeMinutes = item.delivery_time_minutes ?: 0,
                    rating = item.rating ?: 0f,
                    filterIds = item.filterIds.orEmpty()
                )
            }
    }
}