package com.example.vhsrental.ui.screens.spine

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MovieEditActions
import com.example.vhsrental.ui.viewmodels.MoviesViewModel

@Composable
fun ActionButton(
    loginVm: LoginViewModel,
    movieVm: MoviesViewModel,
    modifier: Modifier = Modifier
) {
    val state = loginVm.uiStateFlow.collectAsState().value
    if (state !is LoginUiState.LoggedIn) return

    if (state.user.role == Role.Employee &&
        state.selectedTab == Tab.Movies || state.selectedTab == Tab.Orders
    ) {

        FloatingActionButton(
            onClick = { if (state.selectedTab == Tab.Movies) movieVm.emitAction(MovieActions.AddMovie) },
            modifier = modifier
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add button")
        }
    }
}

