package com.example.vhsrental.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vhsrental.ui.screens.movies.CatalogueScreen
import com.example.vhsrental.ui.screens.orders.CreateOrderScreen
import com.example.vhsrental.ui.screens.orders.OrderDetailScreen
import com.example.vhsrental.ui.screens.orders.OrderScreen
import com.example.vhsrental.ui.screens.users.UsersScreen
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderActions
import com.example.vhsrental.ui.viewmodels.OrderUiState
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UserActions
import com.example.vhsrental.ui.viewmodels.UserUiState
import com.example.vhsrental.ui.viewmodels.UsersViewModel

@Composable
fun OrdersNavigation(
    userVm: UsersViewModel,
    movieVm: MoviesViewModel,
    orderVm: OrderViewModel,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "orders") {
        composable("orders") {
            orderVm.emitOrderAction(OrderActions.OnLoadList)
            OrderScreen(
                orders = (orderVm.uiStateFlow.collectAsState().value as OrderUiState.OrderList).orders,
                users = userVm.getAllUsers(),
                onOrderSelection = { order ->
                    orderVm.emitOrderAction(OrderActions.OnOrderDetailRequest(order))
                    navController.navigate("order_detail")
                },
                onQueryRequest = {}
            )
        }
        composable("order_detail") {
            val order = (orderVm.uiStateFlow.collectAsState().value as OrderUiState.OrderDetail).order
            OrderDetailScreen(
                order = order,
                movie = movieVm.getAllMovies().find { it.id == order.movie },
                user = userVm.getAllUsers().find { it.id == order.user },
                onCloseOrder = { orderVm.emitOrderAction(OrderActions.OnOrderFinish) }
            )
        }
        composable("create_order") {
            orderVm.emitOrderAction(OrderActions.OnCreateOrder)
            CreateOrderScreen(
                state = orderVm.uiStateFlow.collectAsState().value as OrderUiState.OrderCreation,
                onMovieSelection = { navController.navigate("choose_movie") },
                onMovieClear = { orderVm.emitOrderAction(OrderActions.OnClearMovie) },
                onUserSelection = { navController.navigate("choose_user") },
                onUserClear = { orderVm.emitOrderAction(OrderActions.OnClearUser) },
                onConfirm = {
                    orderVm.emitOrderAction(OrderActions.OnOrderConfirm)
                    navController.popBackStack()
                }
            )
        }
        composable("choose_movie") {
            movieVm.emitAction(MovieActions.ToCatalogue)
            CatalogueScreen(
                state = movieVm.uiStateFlow.collectAsState().value,
                toMovieDetail = {
                    orderVm.emitOrderAction(OrderActions.OnMovieUpdate(it))
                    navController.popBackStack()
                },
                onAddMovie = {},
                onQuerryRequest = { /*TODO*/ }
            )
        }
        composable("choose_user") {
            userVm.emitAction(UserActions.OnUserList)
            UsersScreen(
                users = (userVm.uiStateFlow.collectAsState().value as UserUiState.UserList).users,
                toUserDetail = {
                    orderVm.emitOrderAction(OrderActions.OnUserUpdate(it))
                    navController.popBackStack()
                }
            )
        }
    }
}

