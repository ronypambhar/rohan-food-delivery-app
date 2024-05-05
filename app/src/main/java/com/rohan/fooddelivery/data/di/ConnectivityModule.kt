package com.rohan.fooddelivery.data.di

import com.rohan.fooddelivery.data.connectivityUtils.ConnectivityObserver
import com.rohan.fooddelivery.data.connectivityUtils.ConnectivityObserverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    internal abstract fun bindsConnectivityObserver(
        connectivityObserver: ConnectivityObserverImpl,
    ): ConnectivityObserver
}