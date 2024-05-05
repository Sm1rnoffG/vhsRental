package com.example.vhsrental.ui.screens.spine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.data.models.DomainUser

@Composable
fun UserOptions(
    loggedInUser: DomainUser,
    onUserDetail: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val actionsText = "${loggedInUser.name}\n${loggedInUser.surname}"
    var expanded by remember { mutableStateOf(false) }

    Box (
        modifier = modifier
            .wrapContentSize()
    ) {
        Surface (
            modifier = Modifier,
            onClick = { expanded = !expanded }
        ) {
            Text(text = actionsText, textAlign = TextAlign.End)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
        ) {
            DropdownMenuItem(text = { Text(text = "Account detail") }, onClick = onUserDetail)
            DropdownMenuItem(text = { Text(text = "Log out") }, onClick = onLogout)
        }
    }
}
