package com.rohan.fooddelivery.domain.usecases

import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.models.RestaurantsWithFilter
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import java.io.IOException
import javax.inject.Inject

class GetRestaurantsWithFilterUseCase @Inject constructor(
    private val iRestaurantRepository: IRestaurantRepository,
    private val getFilterByIdsUseCase: GetFilterByIdsUseCase
) {

    suspend fun invoke(): RestaurantsWithFilter {
        try {
            val restaurants = iRestaurantRepository.getRestaurants()
            val filterIds = restaurants
                .map { it.filterIds }
                .flatten()
                .distinct()
            val filters = getFilterByIdsUseCase.invoke(filterIds = filterIds)

            restaurants.forEach { restaurantInfo: RestaurantInfo ->
                val filterNames: List<String?> = restaurantInfo.filterIds.map { id ->
                    filters.firstOrNull { it.id == id }?.filterName
                }
                restaurantInfo.filterNames = filterNames.filterNotNull()
            }

            return RestaurantsWithFilter.Data(filters, restaurants)
        } catch (e: IOException) {
            return RestaurantsWithFilter.UnableToReachServer
        } catch (e: Exception) {
            return RestaurantsWithFilter.NoDataFound
        }
    }
}