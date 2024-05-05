package com.rohan.fooddelivery.data.di

import com.rohan.fooddelivery.data.mappers.FilterInfoMapper
import com.rohan.fooddelivery.data.mappers.OpenStatusMapper
import com.rohan.fooddelivery.data.mappers.RestaurantInfoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    fun provideFilterInfoMapper() : FilterInfoMapper {
        return FilterInfoMapper()
    }

    @Provides
    fun provideOpenStatusMapper() : OpenStatusMapper {
        return OpenStatusMapper()
    }

    @Provides
    fun provideRestaurantInfoMapper() : RestaurantInfoMapper {
        return RestaurantInfoMapper()
    }
}