package com.example.vhsrental.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.vhsrental.ui.viewmodels.LoginActions
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.R
import com.example.vhsrental.ui.viewmodels.LoginUiState

@Composable
fun LoginScreen(
    toCatalogue: () -> Unit,
    toRegister: () -> Unit,
    vm: LoginViewModel,
    modifier: Modifier = Modifier,
) {
    val collected = vm.uiStateFlow.collectAsState()
    if (collected.value is LoginUiState.Register) {
        toRegister()
        return
    } else if (collected.value is LoginUiState.LoggedIn) {
        toCatalogue()
        return
    }
    val state = collected.value as LoginUiState.Login

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.loginHeading),
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { vm.emitAction(LoginActions.OnLoginEmailUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.emailPlaceholder)) },
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { vm.emitAction(LoginActions.OnLoginPasswordUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.passwordPlaceholder)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
        )
        if (state.loginError) {
            Text(
                text = state.loginErrorMessage
            )
        }
        Button(
            onClick = { vm.emitAction(LoginActions.OnLoginAttempt) },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.loginButton),
                modifier = Modifier
            )
        }
        Button(
            onClick = { vm.emitAction(LoginActions.OnSwitchScreen) } ,
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.logToRegButton),
                modifier = Modifier
            )
        }
    }
}