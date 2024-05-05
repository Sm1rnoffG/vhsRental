package com.example.vhsrental.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions

@Composable
fun PasswordUpdateScreen(
    loggedIn: LoginUiState.LoggedIn,
    onValueChange: (UpdateAccountActions) -> Unit,
    onConfirm: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        loggedIn.successfulUpdate -> {
            SuccessfulUpdateAlert("Password successfully changed") { onSuccess() }
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "Update password",
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = loggedIn.newPassword,
            label = { Text(text = "New password") },
            onValueChange = { onValueChange(UpdateAccountActions.OnPasswordUpdate(it)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier,
            isError = loggedIn.isError
        )
        OutlinedTextField(
            value = loggedIn.newPasswordRepeat,
            label = { Text(text = "Repeat new password") },
            onValueChange = { onValueChange(UpdateAccountActions.OnPasswordRepeatUpdate(it)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier,
            isError = loggedIn.isError
        )
        OutlinedTextField(
            value = loggedIn.oldPassword,
            label = { Text(text = "Old password") },
            onValueChange = { onValueChange(UpdateAccountActions.OnOldPasswordUpdate(it)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier,
            isError = loggedIn.isError
        )
        if (loggedIn.isError) {
            Text(text = loggedIn.errorMessage)
        }
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(
                text = "Confirm changes",
                textAlign = TextAlign.Center
            )
        }
    }
}
