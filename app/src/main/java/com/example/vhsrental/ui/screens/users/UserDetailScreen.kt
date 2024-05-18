package com.example.vhsrental.ui.screens.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.UserUiState

@Composable
fun UserDetailScreen(
    user: DomainUser,
    usersOrders: List<DomainOrder>,
    modifier: Modifier = Modifier,
    onPromoteClick: () -> Unit = {},
    onDeleteClick: (DomainUser) -> Unit = {},
) {
    val displayDeleteWarning = remember { mutableStateOf(false) }
    val displayPromoteWarning = remember { mutableStateOf(false) }

    val orderCount = if (usersOrders.isEmpty()) "Never" else usersOrders.maxOf { it.createDate }
    val cantDelete = usersOrders.any { it.state != OrderState.Done }

    when {
        displayDeleteWarning.value -> {
            FinalWarning(
                heading = "Permanent deletion!",
                text = "You are about to delete this user permanently",
                onConfirm = { onDeleteClick(user) },
                onCancel = { displayDeleteWarning.value = false }
            )
        }
        displayPromoteWarning.value -> {
            FinalWarning(
                heading = "Promotion to employee!",
                text = "You are about to promote this user to employee role and give " +
                        "them access to the whole app",
                onConfirm = onPromoteClick,
                onCancel = { displayPromoteWarning.value = false }
            )
        }
    }

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(Paddings.medium)
    ) {
        item {
            Text(
                text = "Name: ${user.name}"
            )
        }
        item {
            Text(
                text = "Surname: ${user.surname}"
            )
        }
        item {
            Text(
                text = "E-mail: ${user.email}"
            )
        }
        item {
            Text(
                text = "Role: ${user.role}"
            )
        }
        item {
            Text(
                text = "Latest order on: $orderCount"
            )
        }
        item {
            OrderStats(usersOrders = usersOrders)
        }
        if (user.role == Role.User) {
            item {
                Button(
                    onClick = { displayPromoteWarning.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Promote to employee")
                }
            }
        }
        item {
            Button(
                onClick = { displayDeleteWarning.value = true },
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.LightGray
                ),
                enabled = !cantDelete,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Delete user")
            }
            if (cantDelete) {
                Text(text = "User cannot be deleted if they have opened orders")
            }
        }
    }
}

@Composable
fun OrderStats(
    usersOrders: List<DomainOrder>,
    modifier: Modifier = Modifier,
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(Paddings.small)
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Total orders")
            Text(text = usersOrders.size.toString())
        }
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Ongoing orders")
            Text(text = usersOrders.filter { it.state == OrderState.InProgress }.size.toString())
        }
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Overdue orders")
            Text(text = usersOrders.filter { it.state == OrderState.OverDue }.size.toString())
        }
    }
}

@Composable
fun FinalWarning(
    modifier: Modifier = Modifier,
    heading: String = "",
    text: String = "",
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    AlertDialog(
        title = { Text(text = heading) },
        text = { Text(text = text) },
        onDismissRequest = onCancel,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(text = "Cancel")
            }
        },
        modifier = modifier,
    )
}
