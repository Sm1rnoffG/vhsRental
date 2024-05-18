package com.example.vhsrental.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import com.example.vhsrental.ui.theme.Paddings
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
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(Paddings.medium)
    ) {
        Text(
            text = "Update account information",
            textAlign = TextAlign.Center,
            fontSize = 35.sp
        )
        OutlinedTextField(
            value = loggedIn.newName,
            onValueChange = { onValueChange(UpdateAccountActions.OnNameUpdate(it)) },
            placeholder = { Text(loggedIn.user.name) },
            label = { Text("Name") },
            modifier = Modifier
                .padding(Paddings.small),
        )
        OutlinedTextField(
            value = loggedIn.newSurname,
            onValueChange = { onValueChange(UpdateAccountActions.OnSurnameUpdate(it)) },
            placeholder = { Text(loggedIn.user.surname) },
            label = { Text("Surname") },
            modifier = Modifier
                .padding(Paddings.small),
        )
        OutlinedTextField(
            value = loggedIn.newEmail,
            onValueChange = { onValueChange(UpdateAccountActions.OnEmailUpdate(it)) },
            placeholder = { Text(loggedIn.user.email) },
            label = { Text("E-mail") },
            modifier = Modifier
                .padding(Paddings.small),
            isError = loggedIn.isError
        )
        if (loggedIn.isError) {
            Text(text = loggedIn.errorMessage)
        }
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .padding(Paddings.small)
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
