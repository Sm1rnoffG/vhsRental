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
import com.example.vhsrental.R
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.LoginActions
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel

@Composable
fun RegisterScreen(
    vm: LoginViewModel,
    modifier: Modifier = Modifier,
    toLogin: () -> Unit = {},
) {
    val collected = vm.uiStateFlow.collectAsState()
    if (collected.value is LoginUiState.Login) {
        toLogin()
        return
    }
    val state = collected.value as LoginUiState.Register

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(Paddings.large)
    ) {
        Text(
            text = stringResource(id = R.string.registerHeading),
            fontSize = 35.sp,
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.name,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterNameUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.namePlaceholder)) },
            isError = state.registerError && state.name.isEmpty(),
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.surname,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterSurnameUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.surnamePlaceholder)) },
            isError = state.registerError && state.surname.isEmpty(),
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterEmailUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.emailPlaceholder)) },
            isError = state.registerError && state.email.isEmpty(),
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterPasswordUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.passwordPlaceholder)) },
            visualTransformation = PasswordVisualTransformation(),
            isError = state.registerError && state.password.isEmpty(),
            modifier = Modifier
        )
        OutlinedTextField(
            value = state.passwordRepeat,
            onValueChange = { vm.emitAction(LoginActions.OnRegisterPasswordRepeatUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.repeatPasswordPlaceholder)) },
            visualTransformation = PasswordVisualTransformation(),
            isError = state.registerError && state.passwordRepeat.isEmpty(),
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