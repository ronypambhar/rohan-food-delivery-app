package com.rohan.fooddelivery.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rohan.fooddelivery.R


@Composable
fun NoInternetView(modifier: Modifier) {
    Text(
        modifier = modifier
            .testTag("NoInternetView")
            .padding(16.dp),
        text = stringResource(R.string.no_internet_message),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center
    )
}