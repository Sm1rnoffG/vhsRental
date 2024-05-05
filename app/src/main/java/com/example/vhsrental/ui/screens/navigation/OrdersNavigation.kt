package com.example.vhsrental.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun OrdersNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "orders") {
        composable("orders") {

        }
        composable("orders/order_detail") {

        }
        composable("orders/create_order") {

        }
    }
}