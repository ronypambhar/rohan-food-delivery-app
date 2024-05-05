package com.rohan.fooddelivery.domain.usecases

import com.rohan.fooddelivery.domain.models.RestaurantOpenStatusInfo
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import javax.inject.Inject

class GetOpenStatusUseCase @Inject constructor(
    private val iRestaurantRepository: IRestaurantRepository
) {

    suspend operator fun invoke(restaurantId: String): RestaurantOpenStatusInfo {
        return try {
            val data = iRestaurantRepository.getOpenStatusById(restaurantId = restaurantId)
            RestaurantOpenStatusInfo.Data(data)
        } catch (e: Exception) {
            RestaurantOpenStatusInfo.NoStatusFound
        }
    }
}