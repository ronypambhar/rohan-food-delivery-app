package com.rohan.fooddelivery.presentation.restaurantDetail

import app.cash.turbine.test
import com.rohan.fooddelivery.domain.models.OpenStatusInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.models.RestaurantOpenStatusInfo
import com.rohan.fooddelivery.domain.usecases.GetOpenStatusUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class RestaurantDetailScreenViewModelTest {

    @Mock
    private lateinit var openStatusUseCase: GetOpenStatusUseCase

    private val testDispatchers = StandardTestDispatcher()

    private val screenData = RestaurantDetailScreenViewModel.ScreenData()

    private lateinit var viewModel: RestaurantDetailScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatchers)
        viewModel = RestaurantDetailScreenViewModel(
            openStatusUseCase = openStatusUseCase
        )
    }

    @Test
    fun verify_ViewModel_With_DefaultData() {
        val expectedData = viewModel.screenData.value
        Assert.assertEquals(expectedData.isLoading, screenData.isLoading)
        Assert.assertEquals(expectedData.restaurantInfo, null)
        Assert.assertEquals(expectedData.openStatusInfo, null)
    }

    @Test
    fun verify_GetOpenStatus_with_Data() {
        runTest {
            val mockRestaurant = RestaurantInfo(restaurantId = "1")
            val mockOpenStatusInfo = RestaurantOpenStatusInfo.Data(
                data = OpenStatusInfo(restaurantId = "1", isOpen = true)
            )

            Mockito.`when`(openStatusUseCase(mockRestaurant.restaurantId))
                .thenAnswer { mockOpenStatusInfo }

            val event = RestaurantDetailScreenViewModel.Event.GetOpenStatus(mockRestaurant)

            viewModel.onEvent(event)

            viewModel.screenData.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, true)
                Assert.assertEquals(
                    emittedItem.restaurantInfo?.restaurantId,
                    mockRestaurant.restaurantId
                )

                emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, false)
                Assert.assertTrue(emittedItem.openStatusInfo is RestaurantOpenStatusInfo.Data)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.shutdown()
    }
}