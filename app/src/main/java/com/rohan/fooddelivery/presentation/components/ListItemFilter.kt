package com.rohan.fooddelivery.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rohan.fooddelivery.R
import com.rohan.fooddelivery.domain.models.FilterInfo
import com.rohan.fooddelivery.ui.theme.FoodDeliveryAppTheme
import com.rohan.fooddelivery.ui.theme.LightTextColor
import com.rohan.fooddelivery.ui.theme.SelectedColor
import com.rohan.fooddelivery.ui.theme.TextColor

@Composable
fun FilterItem(filterInfo: FilterInfo, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .height(48.dp)
            .clickable {
                onClick(filterInfo.id)
            },
        shape = RoundedCornerShape(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (filterInfo.isSelected) SelectedColor else MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                model = filterInfo.imageUrl,
                placeholder = painterResource(R.drawable.image_placeholder),
                contentDescription = stringResource(R.string.content_desc_restaurant_filter),
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = Modifier.padding(
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 5.dp
                ),
                text = filterInfo.filterName,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = if (filterInfo.isSelected) LightTextColor else TextColor
                )
            )
        }
    }
    Spacer(modifier = Modifier.width(18.dp))
}

@Preview(showBackground = true)
@Composable
fun FilterItemPreview() {
    FoodDeliveryAppTheme {
        FilterItem(filterInfo = FilterInfo(filterName = "Top Rated").apply { isSelected = true }) {}
    }
}
