package com.rohan.fooddelivery.domain.usecases

import com.rohan.fooddelivery.domain.models.OpenStatusInfo
import com.rohan.fooddelivery.domain.models.RestaurantOpenStatusInfo
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetOpenStatusUseCaseTest {
    @Mock
    private lateinit var iRestaurantRepository: IRestaurantRepository
    private val dispatcher = StandardTestDispatcher()

    private lateinit var useCaseToTest: GetOpenStatusUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCaseToTest = GetOpenStatusUseCase(
            iRestaurantRepository = iRestaurantRepository
        )
    }

    @Test
    fun verifyInvokeWithSuccess() {
        runTest(dispatcher) {
            val restaurantId: String = "1"
            Mockito.`when`(iRestaurantRepository.getOpenStatusById(restaurantId))
                .thenAnswer {
                    OpenStatusInfo(
                     restaurantId = "1",
                        isOpen = true
                    )
                }
            val result  = useCaseToTest.invoke(restaurantId)

            Assert.assertTrue(result is RestaurantOpenStatusInfo.Data)
            Assert.assertEquals((result as RestaurantOpenStatusInfo.Data).data.restaurantId, restaurantId)
        }
    }

    @Test
    fun verifyInvokeWithFailure() {
        runTest(dispatcher) {
            val restaurantId: String = "1"
            Mockito.`when`(iRestaurantRepository.getOpenStatusById(restaurantId))
                .thenAnswer {
                    throw Exception()
                }
            val result  = useCaseToTest.invoke(restaurantId)

            Assert.assertTrue(result is RestaurantOpenStatusInfo.NoStatusFound)
        }
    }
}