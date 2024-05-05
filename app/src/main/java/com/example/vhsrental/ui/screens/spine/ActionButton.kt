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

@Composable
fun ActionButton(vm: LoginViewModel, modifier: Modifier = Modifier) {
    val state = vm.uiStateFlow.collectAsState().value
    if (state !is LoginUiState.LoggedIn) return

    if (state.user.role == Role.Employee &&
        state.selectedTab == Tab.Movies || state.selectedTab == Tab.Orders
    ) {

        FloatingActionButton(
            onClick = { },
            modifier = modifier
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add button")
        }
    }
}

