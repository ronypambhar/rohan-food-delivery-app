package com.rohan.fooddelivery.presentation.restaurantDetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.rohan.fooddelivery.domain.models.OpenStatusInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.models.RestaurantOpenStatusInfo
import com.rohan.fooddelivery.ui.theme.FoodDeliveryAppTheme
import org.junit.Rule
import org.junit.Test


class RestaurantDetailScreenKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val restaurantInfo = RestaurantInfo("1")

    private fun initView(screenData: RestaurantDetailScreenViewModel.ScreenData) {
        composeTestRule.setContent {
            FoodDeliveryAppTheme {
                RestaurantDetailScreen(
                    screenData = screenData,
                    onEvent = {},
                )
            }
        }
    }

    @Test
    fun showLoaderWhenFetchingOpenStatus() {
        initView(RestaurantDetailScreenViewModel.ScreenData(isLoading = true, restaurantInfo = restaurantInfo))
        composeTestRule.onNodeWithTag("LoaderView").assertIsDisplayed()
        composeTestRule.onNodeWithTag("RestaurantOpenStatus").assertDoesNotExist()
        composeTestRule.onNodeWithTag("RestaurantFailStatus").assertDoesNotExist()
    }

    @Test
    fun showStatusViewAfterFetchingOpenStatus() {
        initView(RestaurantDetailScreenViewModel.ScreenData(
            isLoading = false,
            restaurantInfo = restaurantInfo,
            openStatusInfo = RestaurantOpenStatusInfo.Data(data = OpenStatusInfo())))
        composeTestRule.onNodeWithTag("LoaderView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("RestaurantOpenStatus").assertIsDisplayed()
        composeTestRule.onNodeWithTag("RestaurantFailStatus").assertDoesNotExist()
    }

    @Test
    fun showNoStatusViewAfterFetchingOpenStatus() {
        initView(RestaurantDetailScreenViewModel.ScreenData(
            isLoading = false,
            restaurantInfo = restaurantInfo,
            openStatusInfo = RestaurantOpenStatusInfo.NoStatusFound))
        composeTestRule.onNodeWithTag("LoaderView").assertDoesNotExist()
        composeTestRule.onNodeWithTag("RestaurantOpenStatus").assertDoesNotExist()
        composeTestRule.onNodeWithTag("RestaurantFailStatus").assertIsDisplayed()
    }
}