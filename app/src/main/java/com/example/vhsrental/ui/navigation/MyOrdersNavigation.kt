package com.example.vhsrental.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.ui.screens.myorders.MyOrderDetailScreen
import com.example.vhsrental.ui.screens.myorders.MyOrdersScreen
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderActions
import com.example.vhsrental.ui.viewmodels.OrderUiState
import com.example.vhsrental.ui.viewmodels.OrderViewModel

@Composable
fun MyOrdersNavigation(
    orderViewModel: OrderViewModel,
    movieViewModel: MoviesViewModel,
    currentUser: DomainUser,
    navHostController: NavHostController,
) {
    NavHost(navController = navHostController, startDestination = "my_orders") {
        composable("my_orders") {
            orderViewModel.emitOrderAction(OrderActions.OnLoadMyList(currentUser.id))
            val myOrders = (orderViewModel.uiStateFlow.collectAsState().value as OrderUiState.OrderList).orders
            MyOrdersScreen(
                myOrders = movieViewModel.assignMoviesToOrders(myOrders),
                toOrderMyOrderDetail = { orderAndMovie -> orderViewModel.emitOrderAction(OrderActions.OnMyOrderDetailRequest(orderAndMovie)) }
            )
        }
        composable("my_order_detail") {
            val state = orderViewModel.uiStateFlow.collectAsState().value as OrderUiState.MyOrderDetail
            MyOrderDetailScreen(
                order = state.order,
                movie = state.movie,
                onMyOrderAction = { action -> orderViewModel.emitOrderAction(action) }
            )
        }
    }
}