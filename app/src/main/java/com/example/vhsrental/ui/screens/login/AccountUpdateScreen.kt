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
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions

@Composable
fun AccountUpdateScreen(
    loggedIn: LoginUiState.LoggedIn,
    onValueChange: (UpdateAccountActions) -> Unit,
    onConfirm: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        loggedIn.successfulUpdate -> {
            SuccessfulUpdateAlert("Account successfully updated") { onSuccess() }
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "Update account information",
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = loggedIn.newName,
            onValueChange = { onValueChange(UpdateAccountActions.OnNameUpdate(it)) },
            placeholder = { Text(loggedIn.user.name) },
            label = { Text("Name") },
            modifier = Modifier,
        )
        OutlinedTextField(
            value = loggedIn.newSurname,
            onValueChange = { onValueChange(UpdateAccountActions.OnSurnameUpdate(it)) },
            placeholder = { Text(loggedIn.user.surname) },
            label = { Text("Surname") },
            modifier = Modifier,
        )
        OutlinedTextField(
            value = loggedIn.newEmail,
            onValueChange = { onValueChange(UpdateAccountActions.OnEmailUpdate(it)) },
            placeholder = { Text(loggedIn.user.email) },
            label = { Text("E-mail") },
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

@Composable
fun SuccessfulUpdateAlert(
    message: String,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onConfirm() },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(text = "Ok")
            }
        },
        title = { Text(text = "Success!") },
        text = { Text(text = message) },
    )
}
