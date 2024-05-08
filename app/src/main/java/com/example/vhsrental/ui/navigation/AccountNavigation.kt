package com.example.vhsrental.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vhsrental.ui.screens.login.AccountUpdateScreen
import com.example.vhsrental.ui.screens.login.LoggedUserScreen
import com.example.vhsrental.ui.screens.login.PasswordUpdateScreen
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions

@Composable
fun AccountNavigation(loginVm: LoginViewModel, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "accountDetail") {
        composable("accountDetail") {
            val state = loginVm.uiStateFlow.collectAsState().value as LoginUiState.LoggedIn

            LoggedUserScreen(
                user = state.user,
                onUpdateAccountAction = { navController.navigate("editAccount") },
                onUpdatePasswordAction = { navController.navigate("editPassword") }
            )
        }
        composable("editAccount") {
            val state = loginVm.uiStateFlow.collectAsState().value as LoginUiState.LoggedIn

            AccountUpdateScreen(
                loggedIn = state,
                onValueChange = { action -> loginVm.emitActionLoggedIn(action) },
                onConfirm = { loginVm.emitActionLoggedIn(UpdateAccountActions.OnConfirmUpdateData) },
                onSuccess = {
                    loginVm.emitActionLoggedIn(UpdateAccountActions.DismissAlert)
                    navController.popBackStack()
                }
            )
        }
        composable("editPassword") {
            val state = loginVm.uiStateFlow.collectAsState().value as LoginUiState.LoggedIn

            PasswordUpdateScreen(
                loggedIn = state,
                onValueChange = { action -> loginVm.emitActionLoggedIn(action) },
                onConfirm = { loginVm.emitActionLoggedIn(UpdateAccountActions.OnConfirmUpdatePassword) },
                onSuccess = {
                    loginVm.emitActionLoggedIn(UpdateAccountActions.DismissAlert)
                    navController.popBackStack()
                }
            )
        }
    }
}