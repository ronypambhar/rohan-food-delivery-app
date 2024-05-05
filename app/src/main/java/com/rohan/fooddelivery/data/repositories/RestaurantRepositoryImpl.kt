package com.rohan.fooddelivery.data.repositories

import com.rohan.fooddelivery.data.di.IoDispatcher
import com.rohan.fooddelivery.data.mappers.FilterInfoMapper
import com.rohan.fooddelivery.data.mappers.OpenStatusMapper
import com.rohan.fooddelivery.data.mappers.RestaurantInfoMapper
import com.rohan.fooddelivery.data.network.IFoodDeliveryApi
import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.models.OpenStatusInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val iFoodDeliveryApi: IFoodDeliveryApi,
    private val filterInfoMapper: FilterInfoMapper,
    private val restaurantInfoMapper: RestaurantInfoMapper,
    private val openStatusMapper: OpenStatusMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : IRestaurantRepository {

    override suspend fun getFilterByID(filterId: String): FilterInfo {
        return withContext(ioDispatcher) {
            val filterResponse = iFoodDeliveryApi.getFilterById(filterId)
            filterInfoMapper.map(filterResponse)
        }
    }

    override suspend fun getRestaurants(): List<RestaurantInfo> {
        return withContext(ioDispatcher) {
            val restaurantsResponse = iFoodDeliveryApi.getRestaurants()
            restaurantInfoMapper.map(restaurantsResponse)
        }
    }

    override suspend fun getOpenStatusById(restaurantId: String): OpenStatusInfo {
        return withContext(ioDispatcher) {
            val openStatusResponse =
                iFoodDeliveryApi.getOpenStatusByRestaurantId(restaurantId = restaurantId)
            openStatusMapper.map(openStatusResponse)
        }
    }
}