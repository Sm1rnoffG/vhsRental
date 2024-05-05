package com.example.vhsrental.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.R
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions

@Composable
fun PasswordUpdateScreen(
    loggedIn: LoginUiState.LoggedIn,
    modifier: Modifier = Modifier,
    onValueChange: (UpdateAccountActions) -> Unit = {},
    onConfirm: () -> Unit = {},
    onSuccess: () -> Unit = {},
) {
    when {
        loggedIn.successfulUpdate -> {
            SuccessfulUpdateAlert(stringResource(id = R.string.password_alert)) { onSuccess() }
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(id = R.string.update_password_heading),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = loggedIn.newPassword,
            label = { Text(text = stringResource(R.string.new_password_label)) },
            onValueChange = { onValueChange(UpdateAccountActions.OnPasswordUpdate(it)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier,
            isError = loggedIn.isError,
        )
        OutlinedTextField(
            value = loggedIn.newPasswordRepeat,
            label = { Text(text = stringResource(id = R.string.repeat_new_password_label)) },
            onValueChange = { onValueChange(UpdateAccountActions.OnPasswordRepeatUpdate(it)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier,
            isError = loggedIn.isError,
        )
        OutlinedTextField(
            value = loggedIn.oldPassword,
            label = { Text(text = stringResource(R.string.old_password_label)) },
            onValueChange = { onValueChange(UpdateAccountActions.OnOldPasswordUpdate(it)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier,
            isError = loggedIn.isError,
        )
        if (loggedIn.isError) {
            Text(text = loggedIn.errorMessage)
        }
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .wrapContentSize(),
        ) {
            Text(
                text = stringResource(id = R.string.confirm_changes_button),
                textAlign = TextAlign.Center,
            )
        }
    }
}
