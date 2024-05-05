package com.rohan.fooddelivery.presentation.home

import app.cash.turbine.test
import com.rohan.fooddelivery.data.connectivityUtils.ConnectivityObserver
import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.models.RestaurantsWithFilter
import com.rohan.fooddelivery.domain.usecases.FilterRestaurantsBySelectedFiltersUseCase
import com.rohan.fooddelivery.domain.usecases.GetRestaurantsWithFilterUseCase
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
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class HomeScreenViewModelTest {

    @Mock
    private lateinit var connectivityObserver: ConnectivityObserver

    @Mock
    private lateinit var restaurantsUseCase: GetRestaurantsWithFilterUseCase

    @Mock
    private lateinit var filterRestaurantsBySelectedFiltersUseCase: FilterRestaurantsBySelectedFiltersUseCase

    private val testDispatchers = StandardTestDispatcher()

    private val screenData = HomeScreenViewModel.ScreenData()

    private lateinit var viewModel: HomeScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatchers)
        viewModel = HomeScreenViewModel(
            connectionState = connectivityObserver,
            restaurantsUseCase = restaurantsUseCase,
            filterRestaurantsBySelectedFiltersUseCase = filterRestaurantsBySelectedFiltersUseCase
        )
    }

    @Test
    fun verify_ViewModel_With_DefaultData() {
        val expectedData = viewModel.screenData.value
        Assert.assertEquals(expectedData.isLoading, screenData.isLoading)
        Assert.assertEquals(expectedData.restaurantData, screenData.restaurantData)
        Assert.assertEquals(
            expectedData.filteredRestaurants.size,
            screenData.filteredRestaurants.size
        )
        Assert.assertEquals(expectedData.selectedFilters.size, screenData.selectedFilters.size)
    }

    @Test
    fun verify_LoadAllRestaurantsWithAllFilters_with_success_data() {
        runTest {
            val mockRestaurants = getMockRestaurants()
            val mockFilters = getMockFilters()
            Mockito.`when`(restaurantsUseCase.invoke())
                .thenReturn(
                    RestaurantsWithFilter.Data(
                        filters = mockFilters,
                        restaurants = mockRestaurants
                    )
                )
            viewModel.onEvent(HomeScreenViewModel.Event.LoadAllRestaurantsWithAllFilters)

            viewModel.screenData.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, true)
                emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, false)
                Assert.assertTrue(emittedItem.restaurantData is RestaurantsWithFilter.Data)
                Assert.assertEquals(emittedItem.selectedFilters.size, mockFilters.size)
                Assert.assertEquals(emittedItem.filteredRestaurants.size, mockRestaurants.size)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun verify_LoadAllRestaurantsWithAllFilters_with_no_data() {
        runTest {
            Mockito.`when`(restaurantsUseCase.invoke())
                .thenReturn(
                    RestaurantsWithFilter.NoDataFound
                )
            viewModel.onEvent(HomeScreenViewModel.Event.LoadAllRestaurantsWithAllFilters)

            viewModel.screenData.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, true)
                emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, false)
                Assert.assertTrue(emittedItem.selectedFilters.isEmpty())
                Assert.assertTrue(emittedItem.filteredRestaurants.isEmpty())
                Assert.assertTrue(emittedItem.restaurantData is RestaurantsWithFilter.NoDataFound)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun verify_LoadAllRestaurantsWithAllFilters_with_UnableToReachServer() {
        runTest {
            Mockito.`when`(restaurantsUseCase.invoke())
                .thenReturn(
                    RestaurantsWithFilter.UnableToReachServer
                )
            viewModel.onEvent(HomeScreenViewModel.Event.LoadAllRestaurantsWithAllFilters)

            viewModel.screenData.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, true)
                emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, false)
                Assert.assertTrue(emittedItem.selectedFilters.isEmpty())
                Assert.assertTrue(emittedItem.filteredRestaurants.isEmpty())
                Assert.assertTrue(emittedItem.restaurantData is RestaurantsWithFilter.UnableToReachServer)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun verify_FilterClicked_with_updated_selectedFilters_and_filteredRestaurants() {
        runTest {
            val mockRestaurants = getMockRestaurants()
            val mockFilters = getMockFilters()
            Mockito.`when`(restaurantsUseCase.invoke())
                .thenReturn(
                    RestaurantsWithFilter.Data(
                        filters = mockFilters,
                        restaurants = mockRestaurants
                    )
                )

            Mockito.`when`(filterRestaurantsBySelectedFiltersUseCase.invoke(anyList(), anyList()))
                .thenAnswer {
                    listOf(
                        RestaurantInfo(restaurantId = "1", filterIds = listOf("1", "2", "3", "4")),
                        RestaurantInfo(restaurantId = "2", filterIds = listOf("1", "3")),
                    )
                }
            viewModel.onEvent(HomeScreenViewModel.Event.LoadAllRestaurantsWithAllFilters)

            viewModel.screenData.test {
                var emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, true)
                emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.isLoading, false)
                Assert.assertTrue(emittedItem.restaurantData is RestaurantsWithFilter.Data)
                Assert.assertEquals(emittedItem.selectedFilters.size, mockFilters.size)
                Assert.assertEquals(emittedItem.filteredRestaurants.size, mockRestaurants.size)

                viewModel.onEvent(HomeScreenViewModel.Event.FilterClicked("1"))
                emittedItem = awaitItem()
                Assert.assertTrue(emittedItem.selectedFilters.firstOrNull { it.id == "1" }?.isSelected == true)
                emittedItem = awaitItem()
                Assert.assertEquals(emittedItem.filteredRestaurants.size, 2)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun verify_NoInternet_Event() {
        runTest {
            viewModel.onEvent(HomeScreenViewModel.Event.NoInternet)

            viewModel.screenData.test {
                val emittedItem = awaitItem()
                Assert.assertTrue(emittedItem.restaurantData is RestaurantsWithFilter.UnableToReachServer)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.shutdown()
    }

    private fun getMockRestaurants(): List<RestaurantInfo> {
        return listOf(
            RestaurantInfo(restaurantId = "1", filterIds = listOf("1", "2", "3", "4")),
            RestaurantInfo(restaurantId = "2", filterIds = listOf("1", "3")),
            RestaurantInfo(restaurantId = "3", filterIds = listOf("2", "4")),
            RestaurantInfo(restaurantId = "4", filterIds = listOf("2", "3")),
            RestaurantInfo(restaurantId = "5", filterIds = listOf("3")),
        )
    }

    private fun getMockFilters(): List<FilterInfo> {
        return listOf(
            FilterInfo("1", "", ""),
            FilterInfo("2", "", ""),
            FilterInfo("3", "", ""),
            FilterInfo("4", "", ""),
        )
    }
}