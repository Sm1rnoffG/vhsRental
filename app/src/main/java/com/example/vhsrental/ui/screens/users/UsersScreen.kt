package com.example.vhsrental.ui.screens.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.ui.viewmodels.UserUiState

@Composable
fun UsersScreen(
    users: List<DomainUser>,
    toUserDetail: (DomainUser) -> Unit,
    modifier: Modifier = Modifier,
    onQueryRequest: () -> Unit = {},
) {
    val columnState = rememberLazyListState()

    Column (
        modifier = modifier
    ) {
        // QuerryBar()
        LazyColumn (
            state = columnState,
            modifier = Modifier.fillMaxWidth()
        ) {
            users.forEach {
                item {
                    Surface(onClick = { toUserDetail(it) }){
                        UserCard(it)
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: DomainUser, modifier: Modifier = Modifier) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = user.id.toString())
        Text(text = user.email)
        Text(text = "${user.name} ${user.surname}")
    }
}
