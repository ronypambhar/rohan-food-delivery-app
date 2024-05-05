package com.rohan.fooddelivery.presentation.restaurantDetail

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rohan.fooddelivery.domain.models.RestaurantInfo

const val RESTAURANT_DETAIL = "/restaurantDetail?info={info}"

fun NavController.navigateToRestaurantDetailScreen(jsonInfo: String) {
    val route = RESTAURANT_DETAIL.replace("{info}", jsonInfo)
    navigate(route)
}

fun NavGraphBuilder.restaurantDetailScreenNavigation(
    navigateBack: () -> Unit,
    parseJsonInfo: (jsonInfo: String) -> RestaurantInfo
) {
    composable(
        route = RESTAURANT_DETAIL,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                animationSpec = tween(700)
            )
        }
    ) {
        val jsonInfo = it.arguments?.getString("info").orEmpty()
        val restaurantInfo = parseJsonInfo(jsonInfo)
        val viewModel = hiltViewModel<RestaurantDetailScreenViewModel>()
        val screenData = viewModel.screenData.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = Unit) {
            viewModel.onEvent(RestaurantDetailScreenViewModel.Event.GetOpenStatus(restaurantInfo))
        }

        RestaurantDetailScreen(
            screenData = screenData.value,
            onEvent = {
                if (it is RestaurantDetailScreenViewModel.Event.NavigateBack) {
                    navigateBack()
                } else {
                    viewModel.onEvent(event = it)
                }
            }
        )
    }
}