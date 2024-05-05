package com.rohan.fooddelivery.data.repositories

import com.rohan.fooddelivery.data.mappers.FilterInfoMapper
import com.rohan.fooddelivery.data.mappers.OpenStatusMapper
import com.rohan.fooddelivery.data.mappers.RestaurantInfoMapper
import com.rohan.fooddelivery.data.models.FilterResponse
import com.rohan.fooddelivery.data.models.OpenStatusResponse
import com.rohan.fooddelivery.data.models.RestaurantsResponse
import com.rohan.fooddelivery.data.network.IFoodDeliveryApi
import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.models.OpenStatusInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class RestaurantRepositoryImplTest {

    @Mock
    private lateinit var iFoodDeliveryApi: IFoodDeliveryApi

    @Mock
    private lateinit var filterInfoMapper: FilterInfoMapper

    @Mock
    private lateinit var restaurantInfoMapper: RestaurantInfoMapper

    @Mock
    private lateinit var openStatusMapper: OpenStatusMapper

    private lateinit var repositoryToTest: IRestaurantRepository

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repositoryToTest = RestaurantRepositoryImpl(
            iFoodDeliveryApi = iFoodDeliveryApi,
            filterInfoMapper = filterInfoMapper,
            restaurantInfoMapper = restaurantInfoMapper,
            openStatusMapper = openStatusMapper,
            ioDispatcher = dispatcher
        )
    }

    @Test
    fun verify_filterByID() {
        runTest(dispatcher) {
            val filterId = "123"
            val filterResponse = FilterResponse(filterId, "", "")
            val expectedResult = FilterInfo(filterId, "", "")
            Mockito.`when`(iFoodDeliveryApi.getFilterById(filterId))
                .thenReturn(filterResponse)
            Mockito.`when`(filterInfoMapper.map(filterResponse))
                .thenReturn(expectedResult)

            val actualResult = repositoryToTest.getFilterByID(filterId)
            Assert.assertEquals(expectedResult, actualResult)
            verify(filterInfoMapper).map(filterResponse)
        }
    }

    @Test
    fun verify_getRestaurants() {
        runTest(dispatcher) {
            val restaurantsResponse = RestaurantsResponse(
                listOf(
                    RestaurantsResponse.RestaurantItem(id = "1", null, null, null, null, null),
                    RestaurantsResponse.RestaurantItem(id = "2", null, null, null, null, null),
                    RestaurantsResponse.RestaurantItem(id = "3", null, null, null, null, null),
                    RestaurantsResponse.RestaurantItem(id = "4", null, null, null, null, null),
                )
            )
            val expectedResult = listOf(
                RestaurantInfo("1", "", "", 0, 0f, listOf()),
                RestaurantInfo("2", "", "", 0, 0f, listOf()),
                RestaurantInfo("3", "", "", 0, 0f, listOf()),
                RestaurantInfo("4", "", "", 0, 0f, listOf()),
            )
            Mockito.`when`(iFoodDeliveryApi.getRestaurants())
                .thenReturn(restaurantsResponse)
            Mockito.`when`(restaurantInfoMapper.map(restaurantsResponse))
                .thenReturn(expectedResult)

            val actualResult = repositoryToTest.getRestaurants()
            Assert.assertEquals(expectedResult, actualResult)
            verify(restaurantInfoMapper).map(restaurantsResponse)
        }
    }

    @Test
    fun verify_getOpenStatusById() {
        runTest(dispatcher) {
            val restaurantID = "1"
            val openStatusResponse = OpenStatusResponse("1", true)
            val expectedResult = OpenStatusInfo("1", true)
            Mockito.`when`(iFoodDeliveryApi.getOpenStatusByRestaurantId(restaurantId = restaurantID))
                .thenReturn(openStatusResponse)
            Mockito.`when`(openStatusMapper.map(openStatusResponse))
                .thenReturn(expectedResult)

            val actualResult = repositoryToTest.getOpenStatusById(restaurantId = restaurantID)
            Assert.assertEquals(expectedResult, actualResult)
            verify(openStatusMapper).map(openStatusResponse)
        }
    }
}