package com.rohan.fooddelivery.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rohan.fooddelivery.R
import com.rohan.fooddelivery.domain.models.RestaurantsWithFilter
import com.rohan.fooddelivery.presentation.components.AppErrorView
import com.rohan.fooddelivery.presentation.components.AppLoaderView
import com.rohan.fooddelivery.presentation.components.FilterItem
import com.rohan.fooddelivery.presentation.components.ListItemRestaurant
import com.rohan.fooddelivery.presentation.components.NoInternetView

@Composable
fun HomeScreen(
    screenData: HomeScreenViewModel.ScreenData,
    onEvent: (HomeScreenViewModel.Event) -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (screenData.isLoading) {
                AppLoaderView(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            } else {
                when (screenData.restaurantData) {
                    is RestaurantsWithFilter.Data -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("SuccessView")
                        ) {
                            //Filter Section
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 15.dp)
                            ) {
                                items(
                                    items = screenData.selectedFilters,
                                    key = { it.id }
                                ) { item ->
                                    FilterItem(
                                        filterInfo = item,
                                        onClick = { filterId ->
                                            onEvent(HomeScreenViewModel.Event.FilterClicked(filterId))
                                        }
                                    )
                                }
                            }

                            //Restaurants Section
                            if (screenData.filteredRestaurants.isEmpty()) {
                                AppErrorView(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .weight(1f),
                                    text = stringResource(R.string.no_restaurant_found)
                                )
                            } else {
                                LazyColumn(
                                    contentPadding = PaddingValues(
                                        horizontal = 18.dp,
                                        vertical = 10.dp
                                    ),
                                ) {
                                    items(
                                        items = screenData.filteredRestaurants,
                                        key = { it.restaurantId }
                                    ) { item ->
                                        ListItemRestaurant(
                                            restaurant = item,
                                            onClick = {
                                                onEvent(
                                                    HomeScreenViewModel.Event.SelectRestaurant(it)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    RestaurantsWithFilter.NoDataFound -> {
                        AppErrorView(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = stringResource(R.string.no_restaurant_found)
                        )
                    }

                    RestaurantsWithFilter.UnableToReachServer -> {
                        NoInternetView(
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }

                    null -> Unit
                }
            }
        }
    }
}