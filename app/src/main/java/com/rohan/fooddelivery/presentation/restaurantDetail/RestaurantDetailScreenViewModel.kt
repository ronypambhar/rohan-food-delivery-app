package com.rohan.fooddelivery.presentation.restaurantDetail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.domain.models.RestaurantOpenStatusInfo
import com.rohan.fooddelivery.domain.usecases.GetOpenStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailScreenViewModel @Inject constructor(
    private val openStatusUseCase: GetOpenStatusUseCase
) : ViewModel() {

    private val _screenData = MutableStateFlow(ScreenData())
    val screenData: StateFlow<ScreenData> get() = _screenData.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.GetOpenStatus -> {
                _screenData.value = screenData.value.copy(
                    isLoading = true,
                    restaurantInfo = event.restaurantInfo
                )
                viewModelScope.launch {
                    val response = openStatusUseCase(event.restaurantInfo.restaurantId)
                    _screenData.value = screenData.value.copy(
                        isLoading = false,
                        openStatusInfo = response
                    )
                }
            }

            else -> Unit
        }
    }

    sealed class Event {
        data class GetOpenStatus(val restaurantInfo: RestaurantInfo) : Event()
        data object NavigateBack : Event()
    }

    @Immutable
    data class ScreenData(
        val isLoading: Boolean = false,
        val restaurantInfo: RestaurantInfo? = null,
        val openStatusInfo: RestaurantOpenStatusInfo? = null,
    )
}