package com.rohan.fooddelivery.domain.usecases

import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FilterRestaurantsBySelectedFiltersUseCaseTest {

    private lateinit var useCaseTest: FilterRestaurantsBySelectedFiltersUseCase

    @Before
    fun setUp(){
        useCaseTest = FilterRestaurantsBySelectedFiltersUseCase()
    }

    @Test
    fun verify_when_no_filters_selected_then_return_all_restaurants(){
        val mockRestaurants = getMockRestaurants()
        val mockFilters = getMockFilters()

        val result = useCaseTest.invoke(allRestaurants = mockRestaurants, filters = mockFilters)
        Assert.assertEquals(mockRestaurants, result)
    }

    @Test
    fun verify_when_filters_selected_then_return_filtered_restaurants_having_at_least_one_selected_filter(){
        val mockRestaurants = getMockRestaurants()

        //Selected filters with id 2 and 3
        val selectedFilters = getMockFilters().map {
            it.copy().apply { isSelected = it.id == "2" || it.id == "3" }
        }

        //Expecting 4 restaurants from all the restaurants.
        val result = useCaseTest.invoke(allRestaurants = mockRestaurants, filters = selectedFilters)
        Assert.assertEquals(result.size, 4)
    }

    @Test
    fun verify_when_unique_filters_selected_then_return_unique_restaurant(){
        val mockRestaurants = getMockRestaurants()

        //Selected filter with id 5
        val selectedFilters = getMockFilters().map {
            it.copy().apply { isSelected = it.id == "5" }
        }

        //Expecting 1 unique restaurants from all the restaurants.
        val result = useCaseTest.invoke(allRestaurants = mockRestaurants, filters = selectedFilters)
        Assert.assertEquals(result.size, 1)
    }

    private fun getMockRestaurants(): List<RestaurantInfo> {
        return listOf(
            RestaurantInfo(restaurantId = "1", filterIds = listOf("1", "2", "3")),
            RestaurantInfo(restaurantId = "2", filterIds = listOf("1", "3")),
            RestaurantInfo(restaurantId = "3", filterIds = listOf("2", "4")),
            RestaurantInfo(restaurantId = "4", filterIds = listOf("2", "3")),
            RestaurantInfo(restaurantId = "5", filterIds = listOf("5")),
        )
    }

    private fun getMockFilters(): List<FilterInfo> {
        return listOf(
            FilterInfo("1", "", ""),
            FilterInfo("2", "", ""),
            FilterInfo("3", "", ""),
            FilterInfo("4", "", ""),
            FilterInfo("5", "", "")
        )
    }
}