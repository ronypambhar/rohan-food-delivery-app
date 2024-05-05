package com.rohan.fooddelivery.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun AppLoaderView(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .testTag("LoaderView")
    )
}

