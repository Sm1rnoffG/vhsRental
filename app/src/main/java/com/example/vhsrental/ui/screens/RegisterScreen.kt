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
import com.example.vhsrental.R
import com.example.vhsrental.ui.viewmodels.LoginActions
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel

@Composable
fun RegisterScreen (
    toLogin: () -> Unit,
    vm: LoginViewModel,
    modifier: Modifier = Modifier,
) {
    val collected = vm.uiStateFlow.collectAsState()
    if (collected.value is LoginUiState.Login) {
        toLogin()
        return
    }
    val state = collected.value as LoginUiState.Register

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.registerHeading),
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.name,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterNameUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.namePlaceholder)) },
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.surname,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterSurnameUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.surnamePlaceholder)) },
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterEmailUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.emailPlaceholder)) },
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterPasswordUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.passwordPlaceholder)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.passwordRepeat,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterPasswordRepeatUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.repeatPasswordPlaceholder)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
        )
        if (state.registerError) {
            Text(
                text = state.registerErrorMessage,
                modifier = Modifier
            )
        } else if (state.successfulRegister) {
            Text(
                text = stringResource(id = R.string.registerSuccess),
                modifier = Modifier
            )
        }
        Button(
            onClick = { vm.emitAction(LoginActions.OnRegisterAttempt) },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.registerButton),
                modifier = Modifier
            )
        }
        Button(
            onClick = { vm.emitAction(LoginActions.OnSwitchScreen) },
            modifier = Modifier
        ) {
            Text(
                text = stringResource(id = R.string.regToLogButton),
                modifier = Modifier
            )
        }
    }
}