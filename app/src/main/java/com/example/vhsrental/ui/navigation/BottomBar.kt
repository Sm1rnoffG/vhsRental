package com.example.vhsrental.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vhsrental.R
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel


sealed class Tab (val route: String) {
    data object Movies : Tab("movies_tab")
    data object MyOrders : Tab("my_orders_tab")
    data object Orders : Tab("orders_tab")
    data object Login : Tab("login_tab")
    data object Users : Tab("users_tab")
    data object AccountDetail : Tab("account_detail_tab")
}

@Composable
fun BottomBar(
    navController: NavController,
    vm: LoginViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val state = vm.uiStateFlow.collectAsState().value
    if (state !is LoginUiState.LoggedIn ||
        currentDestination?.hierarchy?.any { it.route == Screens.AccountDetail.name } == true)
        return

    val navBarItems = mutableListOf(
        Triple(painterResource(id = R.drawable.baseline_menu_book_24), "Catalogue", Tab.Movies),
        Triple(painterResource(id = R.drawable.baseline_checklist_24), "My Orders", Tab.MyOrders),
    )
    if (state.user.role == Role.Employee) {
        navBarItems.addAll(listOf(
            Triple(painterResource(id = R.drawable.baseline_storefront_24), "All Orders",
                Tab.Orders
            ),
            Triple(painterResource(id = R.drawable.baseline_manage_accounts_24), "Manage Users",
                Tab.Users
            )
        ))
    }

    NavigationBar {
        navBarItems.forEachIndexed { index, triple ->
            NavigationBarItem(
                icon = { Icon(painter = triple.first, contentDescription = "icon $index") },
                label = { Text(text = triple.second) },
                selected = currentDestination?.hierarchy?.any { it.route == triple.third.route } == true,
                onClick = {
                    navController.navigate(triple.third.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}