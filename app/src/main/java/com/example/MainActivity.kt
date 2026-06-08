package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(dynamicColor = false) {
        val viewModel: MainViewModel = viewModel()
        val navController = rememberNavController()

        NavHost(
          navController = navController,
          startDestination = "home"
        ) {
          composable("login") {
             LoginScreen(viewModel, navController)
          }
          composable("home") {
             HomeScreen(viewModel, navController)
          }
          composable("explore") {
             ExploreVibesScreen(viewModel, navController)
          }
          composable("product_detail") {
             ProductDetailScreen(viewModel, navController)
          }
          composable("restaurant_detail") {
             RestaurantDetailScreen(viewModel, navController)
          }
          composable("profile") {
             ProfileScreen(viewModel, navController)
          }
          composable("orders") {
             OrdersScreen(viewModel, navController)
          }
        }
      }
    }
  }
}

