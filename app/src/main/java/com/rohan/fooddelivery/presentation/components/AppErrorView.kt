package com.rohan.fooddelivery.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rohan.fooddelivery.ui.theme.FoodDeliveryAppTheme

@Composable
fun AppErrorView(modifier: Modifier, text: String) {
    Text(
        modifier = modifier
            .testTag("ErrorView")
            .padding(16.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center
    )
}


@Preview(showBackground = true)
@Composable
fun AppErrorViewPreview() {
    FoodDeliveryAppTheme {
        AppErrorView(modifier = Modifier, "No data found" )
    }
}