package com.rohan.fooddelivery.domain.usecases

import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.models.RestaurantsWithFilter
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.io.IOException

class GetRestaurantsWithFilterUseCaseTest {

    @Mock
    private lateinit var iRestaurantRepository: IRestaurantRepository

    @Mock
    private lateinit var getFilterByIdsUseCase: GetFilterByIdsUseCase

    private lateinit var useCaseToTest: GetRestaurantsWithFilterUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCaseToTest = GetRestaurantsWithFilterUseCase(
            iRestaurantRepository = iRestaurantRepository,
            getFilterByIdsUseCase = getFilterByIdsUseCase
        )
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun verifyInvoke_WithData() {
        runTest {
            val mockRestaurants = getMockRestaurants()

            Mockito.`when`(iRestaurantRepository.getRestaurants())
                .thenReturn(mockRestaurants)

            Mockito.`when`(getFilterByIdsUseCase.invoke(filterIds = anyList()))
                .thenAnswer { getMockFilters(it.arguments[0] as List<String>) }

            val result = useCaseToTest.invoke()

            assert(result is RestaurantsWithFilter.Data)
            val data = result as RestaurantsWithFilter.Data
            assert(data.filters.size == 4)
            assert(data.restaurants.size == 5)
            verify(iRestaurantRepository).getRestaurants()
            verify(getFilterByIdsUseCase, times(1)).invoke(anyList())
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun verify_UnableToReachServer_when_getRestaurant_throw_IOException() {
        runTest {
            Mockito.`when`(iRestaurantRepository.getRestaurants())
                .then { throw IOException() }

            val result = useCaseToTest.invoke()

            assert(result is RestaurantsWithFilter.UnableToReachServer)
            verify(iRestaurantRepository).getRestaurants()
            verify(getFilterByIdsUseCase, never()).invoke(anyList())
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun verify_UnableToReachServer_when_getFilterByIdsUseCase_throw_IOException() {
        runTest {
            val mockRestaurants = getMockRestaurants()

            Mockito.`when`(iRestaurantRepository.getRestaurants())
                .thenReturn(mockRestaurants)

            Mockito.`when`(getFilterByIdsUseCase.invoke(anyList()))
                .then { throw IOException() }

            val result = useCaseToTest.invoke()

            assert(result is RestaurantsWithFilter.UnableToReachServer)
            verify(iRestaurantRepository).getRestaurants()
            verify(getFilterByIdsUseCase, times(1)).invoke(anyList())
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun verify_NoDataFound_when_getRestaurant_throw_Exception() {
        runTest {
            Mockito.`when`(iRestaurantRepository.getRestaurants())
                .then { throw Exception() }

            val result = useCaseToTest.invoke()

            assert(result is RestaurantsWithFilter.NoDataFound)
            verify(iRestaurantRepository).getRestaurants()
            verify(getFilterByIdsUseCase, never()).invoke(anyList())
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun verify_NoDataFound_when_getFilterByIdsUseCase_throw_Exception() {
        runTest {
            val mockRestaurants = getMockRestaurants()

            Mockito.`when`(iRestaurantRepository.getRestaurants())
                .thenReturn(mockRestaurants)

            Mockito.`when`(getFilterByIdsUseCase.invoke(anyList()))
                .then { throw Exception() }

            val result = useCaseToTest.invoke()

            assert(result is RestaurantsWithFilter.NoDataFound)
            verify(iRestaurantRepository).getRestaurants()
            verify(getFilterByIdsUseCase, times(1)).invoke(anyList())
        }
    }

    private fun getMockRestaurants(): List<RestaurantInfo> {
        return listOf(
            RestaurantInfo(restaurantId = "1", filterIds = listOf("1", "2", "3", "4")),
            RestaurantInfo(restaurantId = "2", filterIds = listOf("1", "3")),
            RestaurantInfo(restaurantId = "3", filterIds = listOf("2", "4")),
            RestaurantInfo(restaurantId = "4", filterIds = listOf("2", "3")),
            RestaurantInfo(restaurantId = "5", filterIds = listOf("1", "4")),
        )
    }

    private fun getMockFilters(filterIds: List<String>): List<FilterInfo> {
        return filterIds.map {
            FilterInfo(it, "", "")
        }
    }
}