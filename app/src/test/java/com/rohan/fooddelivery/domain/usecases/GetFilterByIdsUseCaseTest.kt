package com.rohan.fooddelivery.domain.usecases

import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.repositories.IRestaurantRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetFilterByIdsUseCaseTest {

    @Mock
    private lateinit var iRestaurantRepository: IRestaurantRepository
    private val dispatcher = StandardTestDispatcher()

    private lateinit var useCaseToTest: GetFilterByIdsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCaseToTest = GetFilterByIdsUseCase(
            iRestaurantRepository = iRestaurantRepository,
            ioDispatcher = dispatcher
        )
    }

    @Test
    fun verifyInvoke() {
        runTest(dispatcher) {
            val filterIds = listOf("1", "2", "3")
            val expectedOutput = listOf(
                FilterInfo("1", "", ""),
                FilterInfo("2", "", ""),
                FilterInfo("3", "", ""),
            )

            Mockito.`when`(iRestaurantRepository.getFilterByID(anyString()))
                .thenAnswer {
                    FilterInfo(it.arguments[0] as String, "", "")
                }

            val result = useCaseToTest.invoke(filterIds)
            result.forEachIndexed { index, filterInfo ->
                Assert.assertEquals(expectedOutput[index], filterInfo)
            }
        }
    }
}