package com.example.vhsrental.ui.navigation

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MovieEditActions
import com.example.vhsrental.ui.viewmodels.MovieEditingViewModel
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderActions
import com.example.vhsrental.ui.viewmodels.OrderCreatingViewModel
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions
import com.example.vhsrental.ui.viewmodels.UsersViewModel


enum class Screens {
    Register,    // Login tab - access only when logged out
    MovieDetail, EditMovie, AddMovie,  // Movie tab - Add movie is side branch
    MyOrderDetail,    // My orders tab
    UserDetail,  // Users Tab
    OrderDetail, CreateOrder, ChooseMovie, ChooseUser,   // Order tab - create order is side branch
    AccountDetail, EditAccount, EditPassword // Accessible always from tob bar
}


@Composable
fun AppSpine(
    movieVm: MoviesViewModel, editMovieVm: MovieEditingViewModel,
    loginVm: LoginViewModel,
    usersVm: UsersViewModel,
    orderVm: OrderViewModel, createOrderVm: OrderCreatingViewModel,
) {
    val navController = rememberNavController()
    val activity = LocalContext.current as Activity

    Scaffold (
        topBar = {
            TopBar(
                vm = loginVm,
                onBackClick = {
                    if (!navController.popBackStack()) activity.finish()
                },
                onAccountDetailClick = {
                    loginVm.emitActionLoggedIn(UpdateAccountActions.OnDisplayDataRequest)
                    navController.navigate(Screens.AccountDetail.name)
                },
                onLogout = {
                    loginVm.emitActionLoggedIn(UpdateAccountActions.OnLogOut)
                    navController.navigate(Tab.Login.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController, loginVm) },
        floatingActionButton = {
            ActionButton(
                loginVm = loginVm,
                navHostController = navController,
                whenOnMoviesClick = { navController.navigate(Screens.AddMovie.name) },
                whenOnOrdersClick = { navController.navigate(Screens.CreateOrder.name) }
            )
        },
    ) { innerPadding ->
        Navigation(
            movieVm = movieVm,
            editMovieVm = editMovieVm,
            loginVm = loginVm,
            usersVm = usersVm,
            orderVm = orderVm,
            orderCreatingVm = createOrderVm,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


