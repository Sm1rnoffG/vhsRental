package com.example.vhsrental.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vhsrental.ui.screens.login.LoginScreen
import com.example.vhsrental.ui.screens.login.RegisterScreen
import com.example.vhsrental.ui.viewmodels.LoginViewModel

@Composable
fun LoginNavigation(vm: LoginViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                toCatalogue = { navController.navigate("login") },
                toRegister = { navController.navigate("register") },
                vm = vm
            )
        }
        composable("register") {
            RegisterScreen(
                toLogin = { navController.popBackStack() },
                vm = vm,
            )
        }
    }
}