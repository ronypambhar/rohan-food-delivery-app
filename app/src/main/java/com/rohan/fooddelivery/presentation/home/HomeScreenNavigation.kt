package com.rohan.fooddelivery.presentation.home

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rohan.fooddelivery.data.connectivityUtils.ConnectivityObserver
import com.rohan.fooddelivery.domain.models.RestaurantInfo

const val HOME = "/home"

fun NavGraphBuilder.homeScreenNavigation(navigateToRestaurantDetail: (restaurant: RestaurantInfo) -> Unit) {
    composable(route = HOME) {
        val viewModel = hiltViewModel<HomeScreenViewModel>()
        val screenData = viewModel.screenData.collectAsStateWithLifecycle()
        val connectionState by viewModel.connectionState
            .observe()
            .collectAsState(
                initial = ConnectivityObserver.Status.Unknown
            )

        LaunchedEffect(key1 = connectionState) {
            if(connectionState == ConnectivityObserver.Status.Available) {
                viewModel.onEvent(HomeScreenViewModel.Event.LoadAllRestaurantsWithAllFilters)
            } else {
                viewModel.onEvent(HomeScreenViewModel.Event.NoInternet)
            }
        }

        HomeScreen(
            screenData = screenData.value,
            onEvent = {
                if (it is HomeScreenViewModel.Event.SelectRestaurant) {
                    navigateToRestaurantDetail.invoke(it.restaurant)
                } else {
                    viewModel.onEvent(it)
                }
            }
        )
    }
}