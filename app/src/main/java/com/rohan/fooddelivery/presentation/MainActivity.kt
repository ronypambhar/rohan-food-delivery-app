package com.rohan.fooddelivery.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.rohan.fooddelivery.domain.models.RestaurantInfo
import com.rohan.fooddelivery.presentation.home.HOME
import com.rohan.fooddelivery.presentation.home.homeScreenNavigation
import com.rohan.fooddelivery.presentation.restaurantDetail.navigateToRestaurantDetailScreen
import com.rohan.fooddelivery.presentation.restaurantDetail.restaurantDetailScreenNavigation
import com.rohan.fooddelivery.ui.theme.FoodDeliveryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var gsonConvertor: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodDeliveryAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = HOME
                    ) {
                        homeScreenNavigation(navigateToRestaurantDetail = {
                            val jsonInfo = gsonConvertor.toJson(it)
                            navController.navigateToRestaurantDetailScreen(jsonInfo)
                        })
                        restaurantDetailScreenNavigation(
                            navigateBack = { navController.popBackStack() },
                            parseJsonInfo = { jsonInfo ->
                                gsonConvertor.fromJson(jsonInfo, RestaurantInfo::class.java)
                            }
                        )
                    }
                }
            }
        }
    }
}