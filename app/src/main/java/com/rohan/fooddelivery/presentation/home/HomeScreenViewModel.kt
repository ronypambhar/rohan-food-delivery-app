package com.rohan.fooddelivery.presentation.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohan.fooddelivery.data.connectivityUtils.ConnectivityObserver
import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.models.RestaurantsWithFilter
import com.rohan.fooddelivery.domain.usecases.FilterRestaurantsBySelectedFiltersUseCase
import com.rohan.fooddelivery.domain.usecases.GetRestaurantsWithFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val connectionState: ConnectivityObserver,
    private val restaurantsUseCase: GetRestaurantsWithFilterUseCase,
    private val filterRestaurantsBySelectedFiltersUseCase: FilterRestaurantsBySelectedFiltersUseCase
) : ViewModel() {

    private val _screenData = MutableStateFlow(ScreenData())
    val screenData: StateFlow<ScreenData> get() = _screenData.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            Event.LoadAllRestaurantsWithAllFilters -> {
                _screenData.value = screenData.value.copy(
                    isLoading = true
                )
                viewModelScope.launch {
                    val response = restaurantsUseCase.invoke()
                    _screenData.value = if (response is RestaurantsWithFilter.Data) {
                        screenData.value.copy(
                            isLoading = false,
                            restaurantData = response,
                            selectedFilters = response.filters.toMutableStateList(),
                            filteredRestaurants = response.restaurants.toMutableStateList(),
                        )
                    } else {
                        screenData.value.copy(
                            isLoading = false,
                            restaurantData = response,
                            selectedFilters = mutableStateListOf(),
                            filteredRestaurants = mutableStateListOf()
                        )
                    }
                }
            }

            is Event.FilterClicked -> {
                /**
                 * Update [FilterInfo] selection.
                 */
                val selectedFilters = screenData.value.selectedFilters
                    .apply {
                        firstOrNull { it.id == event.filterId }
                            ?.apply { isSelected = isSelected.not() }
                    }.toMutableStateList()

                _screenData.value = screenData.value.copy(
                    selectedFilters = selectedFilters
                )

                onEvent(Event.GetRestaurantsBySelectedFilter)
            }

            is Event.GetRestaurantsBySelectedFilter -> {
                (screenData.value.restaurantData as? RestaurantsWithFilter.Data)?.let { data ->
                    val filteredRestaurants = filterRestaurantsBySelectedFiltersUseCase.invoke(
                        data.restaurants,
                        screenData.value.selectedFilters
                    )
                    _screenData.value = screenData.value.copy(
                        filteredRestaurants = filteredRestaurants.toMutableStateList()
                    )
                }
            }

            is Event.NoInternet -> {
                _screenData.value = screenData.value.copy(
                    restaurantData = RestaurantsWithFilter.UnableToReachServer
                )
            }

            else -> Unit
        }
    }

    sealed class Event {
        data object LoadAllRestaurantsWithAllFilters : Event()
        data class FilterClicked(val filterId: String) : Event()
        data object GetRestaurantsBySelectedFilter : Event()
        data class SelectRestaurant(val restaurant: RestaurantInfo) : Event()
        data object NoInternet : Event()
    }

    @Immutable
    data class ScreenData(
        val isLoading: Boolean = false,
        val restaurantData: RestaurantsWithFilter? = null,
        val filteredRestaurants: SnapshotStateList<RestaurantInfo> = mutableStateListOf(),
        val selectedFilters: SnapshotStateList<FilterInfo> = mutableStateListOf()
    )
}