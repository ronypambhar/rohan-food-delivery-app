package com.rohan.fooddelivery.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.models.RestaurantsWithFilter
import com.rohan.fooddelivery.ui.theme.FoodDeliveryAppTheme
import org.junit.Rule
import org.junit.Test


class HomeScreenKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun initView(screenData: HomeScreenViewModel.ScreenData) {
        composeTestRule.setContent {
            FoodDeliveryAppTheme {
                HomeScreen(
                    screenData = screenData,
                    onEvent = {},
                )
            }
        }
    }

    @Test
    fun showLoaderWhenFetchingRestaurant() {
        initView(HomeScreenViewModel.ScreenData(isLoading = true))
        composeTestRule.onNodeWithTag("LoaderView").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SuccessView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("ErrorView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("NoInternetView").assertDoesNotExist()
    }

    @Test
    fun showSuccessAfterFetchingRestaurantData() {
        initView(HomeScreenViewModel.ScreenData(isLoading = false, restaurantData = RestaurantsWithFilter.Data(), filteredRestaurants = listOf(RestaurantInfo()).toMutableStateList()))
        composeTestRule.onNodeWithTag("LoaderView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("SuccessView").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ErrorView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("NoInternetView").assertDoesNotExist()
    }

    @Test
    fun showErrorWhenNoFilterRestaurantFound() {
        initView(HomeScreenViewModel.ScreenData(isLoading = false, restaurantData = RestaurantsWithFilter.Data(), filteredRestaurants = mutableStateListOf()))
        composeTestRule.onNodeWithTag("LoaderView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("SuccessView").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ErrorView").assertIsDisplayed()
        composeTestRule.onNodeWithTag("NoInternetView").assertDoesNotExist()
    }

    @Test
    fun showErrorWhenNoDataFound() {
        initView(HomeScreenViewModel.ScreenData(isLoading = false, restaurantData = RestaurantsWithFilter.NoDataFound))
        composeTestRule.onNodeWithTag("LoaderView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("SuccessView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("ErrorView").assertIsDisplayed()
        composeTestRule.onNodeWithTag("NoInternetView").assertDoesNotExist()
    }

    @Test
    fun showNoInternetViewWhenNoInternetConnection() {
        initView(HomeScreenViewModel.ScreenData(isLoading = false, restaurantData = RestaurantsWithFilter.UnableToReachServer))
        composeTestRule.onNodeWithTag("LoaderView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("SuccessView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("ErrorView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("NoInternetView").assertIsDisplayed()
    }
}