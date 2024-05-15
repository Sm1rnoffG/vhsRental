package com.example.vhsrental.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel

@Composable
fun ActionButton(
    loginVm: LoginViewModel,
    navHostController: NavHostController,
    whenOnOrdersClick: () -> Unit,
    whenOnMoviesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = loginVm.uiStateFlow.collectAsState().value
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestinationRoute = navBackStackEntry?.destination?.route

    if (state !is LoginUiState.LoggedIn || (currentDestinationRoute != Tab.Movies.route &&
                currentDestinationRoute != Tab.Orders.route))
        return

    FloatingActionButton(
        onClick = when (currentDestinationRoute) {
            Tab.Movies.route ->  whenOnMoviesClick
            else -> whenOnOrdersClick
        },
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "add button")
    }
}

