package com.example.vhsrental.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.ui.screens.users.FinalWarning

@Composable
fun LoggedUserScreen(
    user: DomainUser,
    cantDelete: Boolean,
    modifier: Modifier = Modifier,
    onUpdateAccountAction: () -> Unit = {},
    onUpdatePasswordAction: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
) {
    val displayDeleteWarning = remember { mutableStateOf(false) }

    when {
        displayDeleteWarning.value -> {
            FinalWarning(
                heading = "Permanent deletion!",
                text = "You are about to delete your account permanently",
                onConfirm = onDeleteAccount,
                onCancel = { displayDeleteWarning.value = false }
            )
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = user.name)
        Text(text = user.surname)
        Text(text = "Contact information")
        Text(text = user.email)

        Button(
            onClick = onUpdateAccountAction,
            modifier = Modifier
        ) {
            Text(text = "Update account information")
        }

        Button(
            onClick = onUpdatePasswordAction,
            modifier = Modifier
        ) {
            Text(text = "Update password")
        }

        Button(
            onClick = { displayDeleteWarning.value = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            enabled = !cantDelete
        ) {
            Text(text = "Delete Account")
        }
        if (cantDelete) {
            Text(
                text = "Your account cannot be deleted while you have opened orders",
                textAlign = TextAlign.Center,
                color = Color.LightGray
            )
        }
    }
}