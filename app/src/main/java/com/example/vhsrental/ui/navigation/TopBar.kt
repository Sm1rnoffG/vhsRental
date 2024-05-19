package com.example.vhsrental.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vhsrental.R
import com.example.vhsrental.ui.screens.query.QueryBar
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    loginVm: LoginViewModel,
    movieVm: MoviesViewModel,
    userVm: UsersViewModel,
    orderVm: OrderViewModel,
    navController: NavHostController,
    onLogout: () -> Unit,
    onCloseApp: () -> Unit,
    onAccountDetailClick: () -> Unit,
) {
    val state = loginVm.uiStateFlow.collectAsState().value
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationRoute = navBackStackEntry?.destination?.route
    val queryTab = when (currentDestinationRoute) {
        Tab.Movies.route -> Tab.Movies
        Tab.Users.route -> Tab.Users
        Tab.Orders.route -> Tab.Orders
        Tab.MyOrders.route -> Tab.MyOrders
        else -> null
    }
    val onBackClick = when (currentDestinationRoute) {
        Tab.Movies.route -> onCloseApp
        Tab.Users.route -> onCloseApp
        Tab.Orders.route -> onCloseApp
        Tab.MyOrders.route -> onCloseApp
        Tab.Login.route -> onCloseApp
        else -> { { navController.popBackStack() } }
    }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Icon(
                    painterResource(id = R.drawable.vhs_tape_svgrepo_com),
                    contentDescription = stringResource(id = R.string.shop_icon),
                    modifier = Modifier
                        .size(40.dp)
                )
            },
            navigationIcon = { NavigationButton(vm = loginVm) { onBackClick() } },
            actions = {
                if (state is LoginUiState.LoggedIn) {
                    UserOptions(
                        loggedInUser = state.user,
                        onUserDetail = onAccountDetailClick,
                        onLogout = onLogout
                    )
                } else {
                    Text(text = stringResource(id = R.string.not_signed_in))
                }
            },
        )

        if (queryTab != null && state is LoginUiState.LoggedIn) {
            QueryBar(
                movieVm = movieVm,
                orderVm = orderVm,
                usersVm = userVm,
                displayedTab = queryTab,
                currentUser = state.user
            )
        }
    }
}

@Composable
fun NavigationButton(vm: LoginViewModel, onBackClick: () -> Unit) {
    IconButton(
        onClick = onBackClick
    ) {
        Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
    }
}
