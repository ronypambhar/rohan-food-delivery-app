package com.rohan.fooddelivery.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rohan.fooddelivery.R
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.ui.theme.FoodDeliveryAppTheme
import com.rohan.fooddelivery.ui.theme.RedColor
import com.rohan.fooddelivery.ui.theme.YellowColor

@Composable
fun ListItemRestaurant(restaurant: RestaurantInfo, onClick: (RestaurantInfo) -> Unit) {
    Card(
        elevation = cardElevation(4.dp),
        shape = RoundedCornerShape(
            topEnd = 12.dp,
            topStart = 12.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick(restaurant)
            }

    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp),
                model = restaurant.imageUrl,
                placeholder = painterResource(R.drawable.image_placeholder),
                contentDescription = stringResource(R.string.content_desc_restaurant_logo),
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = restaurant.restaurantName,
                    style = MaterialTheme.typography.titleLarge
                )
                Row {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.Star,
                        contentDescription = stringResource(R.string.content_desc_restaurant_rating),
                        tint = YellowColor
                    )
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = restaurant.rating.toString(),
                        style = MaterialTheme.typography.displaySmall
                    )
                }

            }

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = restaurant.generateFilterName(),
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    painter = painterResource(id = R.drawable.clock),
                    tint = RedColor,
                    contentDescription = stringResource(R.string.content_desc_restaurant_rating)
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = restaurant.deliveryTimeMinutes.toString() + " mins",
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Preview(showBackground = true)
@Composable
fun RestaurantItemPreview() {
    FoodDeliveryAppTheme {
        ListItemRestaurant(
            restaurant = RestaurantInfo(
                restaurantName = "Wayne's Smelly Eggs",
                rating = 5f,
                deliveryTimeMinutes = 15
            ),
            onClick = {}
        )
    }
}