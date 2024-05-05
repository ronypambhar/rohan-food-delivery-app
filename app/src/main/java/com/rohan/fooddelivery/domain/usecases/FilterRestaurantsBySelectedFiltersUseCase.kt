package com.rohan.fooddelivery.domain.usecases

import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import javax.inject.Inject

class FilterRestaurantsBySelectedFiltersUseCase @Inject constructor(){

    operator fun invoke(
        allRestaurants: List<RestaurantInfo>,
        filters: List<FilterInfo>,
    ): List<RestaurantInfo> {
        /**
         * Get selected filters
         */
        val selectedFilters = filters.filter { it.isSelected }

        return if (selectedFilters.isEmpty()) {
            allRestaurants
        } else {
            /**
             * Filter restaurants with at least one selected filter.
             */
            allRestaurants.filter { restaurantInfo ->
                selectedFilters.any { filterInfo ->
                    restaurantInfo.filterIds.contains(filterInfo.id)
                }
            }
        }
    }
}