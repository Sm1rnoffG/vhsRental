package com.example.vhsrental.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions

@Composable
fun LoggedUserScreen(
    user: DomainUser,
    modifier: Modifier = Modifier,
    onUpdateAccountAction: () -> Unit = {},
    onUpdatePasswordAction: () -> Unit = {},
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = user.name)
        Text(text = user.surname)
        Text(text = "Contact information")
        Text(text = user.email)

        Row (
            horizontalArrangement = Arrangement.Center
        ) {
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
        }
    }
}