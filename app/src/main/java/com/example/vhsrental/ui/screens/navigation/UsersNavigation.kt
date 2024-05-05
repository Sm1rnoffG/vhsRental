package com.example.vhsrental.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel

@Composable
fun UsersNavigation(loginVm: LoginViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "users") {
        composable("users") {

        }
        composable("users/user_detail") {

        }
    }
}