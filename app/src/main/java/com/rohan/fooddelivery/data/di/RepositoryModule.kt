package com.rohan.fooddelivery.data.di

import com.rohan.fooddelivery.data.mappers.FilterInfoMapper
import com.rohan.fooddelivery.data.mappers.OpenStatusMapper
import com.rohan.fooddelivery.data.mappers.RestaurantInfoMapper
import com.rohan.fooddelivery.data.network.IFoodDeliveryApi
import com.rohan.fooddelivery.data.repositories.RestaurantRepositoryImpl
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRestaurantRepository(
        iFoodDeliveryApi: IFoodDeliveryApi,
        filterInfoMapper: FilterInfoMapper,
        restaurantInfoMapper: RestaurantInfoMapper,
        openStatusMapper: OpenStatusMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) : IRestaurantRepository {
        return RestaurantRepositoryImpl(
            iFoodDeliveryApi = iFoodDeliveryApi,
            filterInfoMapper = filterInfoMapper,
            restaurantInfoMapper = restaurantInfoMapper,
            openStatusMapper = openStatusMapper,
            ioDispatcher = ioDispatcher
        )
    }
}