package com.example.vhsrental.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import com.example.vhsrental.ui.viewmodels.LoginActions
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.R
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.LoginUiState

@Composable
fun LoginScreen(
    vm: LoginViewModel,
    modifier: Modifier = Modifier,
    toCatalogue: () -> Unit = {},
    toRegister: () -> Unit = {},
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(Paddings.large)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.loginHeading),
            fontSize = 35.sp,
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { vm.emitAction(LoginActions.OnLoginEmailUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.emailPlaceholder)) },
            modifier = Modifier
                .padding(Paddings.small),
            isError = state.loginError && state.email.isEmpty()
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { vm.emitAction(LoginActions.OnLoginPasswordUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.passwordPlaceholder)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .padding(Paddings.small),
            isError = state.loginError && state.password.isEmpty()
        )
        if (state.loginError) {
            Text(
                text = state.loginErrorMessage
            )
        }
        Button(
            onClick = { vm.emitAction(LoginActions.OnLoginAttempt) },
            modifier = Modifier
                .padding(Paddings.small),
        ) {
            Text(
                text = stringResource(id = R.string.loginButton),
                modifier = Modifier
            )
        }
        Button(
            onClick = { vm.emitAction(LoginActions.OnSwitchScreen) },
            modifier = Modifier
                .padding(Paddings.small),
        ) {
            Text(
                text = stringResource(id = R.string.logToRegButton),
                modifier = Modifier
            )
        }
    }
}