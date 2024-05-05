package com.example.vhsrental.ui.screens.spine

import android.app.Activity
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.vhsrental.R
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(vm: LoginViewModel, navController: NavHostController) {
    val state = vm.uiStateFlow.collectAsState().value

    CenterAlignedTopAppBar(
        title = { Icon(painterResource(id = R.drawable.baseline_storefront_24), contentDescription = "shopIcon") },
        navigationIcon = { NavigationButton(vm = vm, navController = navController) },
        actions = {
            if (state is LoginUiState.LoggedIn) {
                UserOptions(
                    state.user,
                    { vm.emitActionLoggedIn(UpdateAccountActions.OnDisplayDataRequest) },
                    { vm.emitActionLoggedIn(UpdateAccountActions.OnLogOut) }
                )
            } else {
                Text(text = "Not signed in")
            }
        },
    )
}

@Composable
fun NavigationButton(vm: LoginViewModel, navController: NavHostController) {
    val activity = LocalContext.current as? Activity
    val state = vm.uiStateFlow.collectAsState().value
    val stackTop = navController.currentBackStackEntry

    IconButton(
        onClick = {
            if (state is LoginUiState.LoggedIn && state.selectedTab == Tab.AccountDetail) {
                vm.emitActionLoggedIn(UpdateAccountActions.OnSwitchTab(Tab.Movies))
            } else if (stackTop == null) {
                activity?.finish()  // Exit app
            } else {
                navController.popBackStack()
            }
        }
    ) {
        Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_24), null)
    }
}
