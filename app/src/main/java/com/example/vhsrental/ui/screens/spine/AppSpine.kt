package com.example.vhsrental.ui.screens.spine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.vhsrental.ui.navigation.AccountNavigation
import com.example.vhsrental.ui.navigation.LoginNavigation
import com.example.vhsrental.ui.navigation.MoviesNavigation
import com.example.vhsrental.ui.navigation.MyOrdersNavigation
import com.example.vhsrental.ui.navigation.OrdersNavigation
import com.example.vhsrental.ui.navigation.UsersNavigation
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UsersViewModel
import kotlin.math.log

enum class Tab {
    Movies,
    MyOrders,
    Orders,
    Users,
    AccountDetail,
}

@Composable
fun AppSpine(
    movieVm: MoviesViewModel,
    loginVm: LoginViewModel,
    usersVm: UsersViewModel,
    orderVm: OrderViewModel
) {
    val state = loginVm.uiStateFlow.collectAsState().value
    val navController = rememberNavController()

    Scaffold (
        topBar = { TopBar(vm = loginVm, navController = navController) },
        bottomBar = { BottomBar(vm = loginVm) },
        floatingActionButton = { ActionButton(loginVm, movieVm) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            if (state is LoginUiState.LoggedIn) {
                when (state.selectedTab) {
                    Tab.Movies -> MoviesNavigation(
                        navController = navController,
                        movieVm = movieVm,
                        user = state.user,
                        orderVm = orderVm
                    )
                    Tab.MyOrders -> MyOrdersNavigation(
                        orderViewModel = orderVm,
                        movieViewModel = movieVm,
                        currentUser = state.user,
                        navHostController = navController
                    )
                    Tab.Orders -> OrdersNavigation(
                        userVm = usersVm,
                        movieVm = movieVm,
                        orderVm = orderVm,
                        currentUser = state.user,
                        navController = navController
                    )
                    Tab.Users -> UsersNavigation(
                        loginVm = loginVm,
                        userVm = usersVm,
                        orderVm = orderVm,
                        navController = navController
                    )
                    Tab.AccountDetail -> AccountNavigation(
                        loginVm = loginVm,
                        navController = navController
                    )
                }
            } else {
                LoginNavigation(vm = loginVm, navController = navController)
            }
        }
    }
}
