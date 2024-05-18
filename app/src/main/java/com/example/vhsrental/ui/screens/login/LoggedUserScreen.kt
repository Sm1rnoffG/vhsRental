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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.R
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
                heading = stringResource(id = R.string.permanent_deletion),
                text = stringResource(id = R.string.self_delete_warning),
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
        Text(text = stringResource(R.string.user_contact_info))
        Text(text = user.email)

        Button(
            onClick = onUpdateAccountAction,
            modifier = Modifier
        ) {
            Text(text = stringResource(id = R.string.update_account_button))
        }

        Button(
            onClick = onUpdatePasswordAction,
            modifier = Modifier
        ) {
            Text(text = stringResource(id = R.string.update_password_button))
        }

        Button(
            onClick = { displayDeleteWarning.value = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            enabled = !cantDelete
        ) {
            Text(text = stringResource(id = R.string.delete_account_button))
        }
        if (cantDelete) {
            Text(
                text = stringResource(id = R.string.cant_delete_self),
                textAlign = TextAlign.Center,
                color = Color.LightGray
            )
        }
    }
}