package com.example.vhsrental.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vhsrental.ui.screens.users.UserDetailScreen
import com.example.vhsrental.ui.screens.users.UsersScreen
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.OrderActions
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UserActions
import com.example.vhsrental.ui.viewmodels.UserUiState
import com.example.vhsrental.ui.viewmodels.UsersViewModel
import kotlin.math.log

@Composable
fun UsersNavigation(
    loginVm: LoginViewModel,
    userVm: UsersViewModel,
    orderVm: OrderViewModel,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "users") {
        composable("users") {
            val currentUser = (loginVm.uiStateFlow.collectAsState().value as LoginUiState.LoggedIn).user
            userVm.emitAction(UserActions.OnUserList(currentUser))

            UsersScreen(
                users = (userVm.uiStateFlow.collectAsState().value as UserUiState.UserList).users,
                toUserDetail = { user ->
                    userVm.emitAction(UserActions.OnUserDetail(user))
                    navController.navigate("user_detail")
                }
            )
        }
        composable("user_detail") {
            val state = userVm.uiStateFlow.collectAsState().value as UserUiState.UserDetail

            UserDetailScreen(
                user = state.user,
                usersOrders = orderVm.getUsersOrders(state.user.id),
                onPromoteClick = { userVm.emitAction(UserActions.OnPromoteUser) },
                onDeleteClick = {
                    navController.popBackStack()
                    orderVm.emitOrderAction(OrderActions.OnDeleteUser(state.user.id))
                    userVm.emitAction(UserActions.OnDeleteUser)
                }
            )
        }
    }
}