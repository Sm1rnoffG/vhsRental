package com.example.vhsrental.ui.navigation

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import com.example.vhsrental.R
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    vm: LoginViewModel,
    onLogout: () -> Unit,
    onAccountDetailClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state = vm.uiStateFlow.collectAsState().value

    CenterAlignedTopAppBar(
        title = { Icon(painterResource(id = R.drawable.baseline_storefront_24), contentDescription = "shopIcon") },
        navigationIcon = { NavigationButton(vm = vm, onBackClick) },
        actions = {
            if (state is LoginUiState.LoggedIn) {
                UserOptions(
                    loggedInUser = state.user,
                    onUserDetail = onAccountDetailClick,
                    onLogout = onLogout
                )
            } else {
                Text(text = "Not signed in")
            }
        },
    )
}

@Composable
fun NavigationButton(vm: LoginViewModel, onBackClick: () -> Unit) {
    IconButton(
        onClick = onBackClick
    ) {
        Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
    }
}
