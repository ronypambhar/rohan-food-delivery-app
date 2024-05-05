package com.rohan.fooddelivery.presentation.restaurantDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rohan.fooddelivery.R
import com.rohan.fooddelivery.domain.models.RestaurantOpenStatusInfo
import com.rohan.fooddelivery.ui.theme.NegativeColor
import com.rohan.fooddelivery.ui.theme.PositiveColor
import com.rohan.fooddelivery.ui.theme.SubtitlesColor

@Composable
fun RestaurantDetailScreen(
    screenData: RestaurantDetailScreenViewModel.ScreenData,
    onEvent: (RestaurantDetailScreenViewModel.Event) -> Unit
) {
    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (screenData.restaurantInfo != null) {
                val restaurantInfo = screenData.restaurantInfo

                AsyncImage(
                    model = restaurantInfo.imageUrl,
                    placeholder = painterResource(R.drawable.image_placeholder),
                    contentDescription = stringResource(R.string.content_desc_restaurant_logo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                IconButton(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .testTag("BackButton"),
                    onClick = {
                        onEvent(RestaurantDetailScreenViewModel.Event.NavigateBack)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "NavigateBack",
                        modifier = Modifier.size(50.dp)

                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 200.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = restaurantInfo.restaurantName,
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(
                            text = screenData.restaurantInfo?.generateFilterName().orEmpty(),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = SubtitlesColor
                            )
                        )
                        if (screenData.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.testTag("LoaderView")
                            )
                        } else {
                            when (val status = screenData.openStatusInfo) {
                                is RestaurantOpenStatusInfo.Data -> {
                                    Text(
                                        modifier = Modifier.testTag("RestaurantOpenStatus"),
                                        text = stringResource(if (status.data.isOpen) R.string.open_status else R.string.close_status),
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            color = if (status.data.isOpen) PositiveColor else NegativeColor
                                        )
                                    )
                                }

                                RestaurantOpenStatusInfo.NoStatusFound -> {
                                    Text(
                                        modifier = Modifier.testTag("RestaurantFailStatus"),
                                        text = stringResource(R.string.no_open_status),
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            color = NegativeColor
                                        )
                                    )
                                }

                                null -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}