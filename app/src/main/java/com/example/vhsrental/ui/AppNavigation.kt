package com.example.vhsrental.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vhsrental.ui.screens.LoginScreen
import com.example.vhsrental.ui.screens.RegisterScreen
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UsersViewModel

@Composable
fun AppNavigation(
    movieVM: MoviesViewModel,
    userVM: UsersViewModel,
    orderVM: OrderViewModel,
    loginVM: LoginViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                toCatalogue = { navController.navigate("greet") },
                toRegister = { navController.navigate("register") },
                vm = loginVM
            )
        }
        composable("register") {
            RegisterScreen(
                toLogin = { navController.navigate("login") },
                vm = loginVM
            )
        }
        composable("greet") {
            Greeting(vm = loginVM)
        }
    }
}