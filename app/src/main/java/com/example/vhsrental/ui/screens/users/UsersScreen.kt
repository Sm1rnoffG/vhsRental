package com.example.vhsrental.ui.screens.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vhsrental.R
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.ui.screens.EmptyQuery
import com.example.vhsrental.ui.theme.Paddings

@Composable
fun UsersScreen(
    users: List<DomainUser>,
    toUserDetail: (DomainUser) -> Unit,
    modifier: Modifier = Modifier,
) {
    val columnState = rememberLazyListState()

    if (users.isEmpty()) {
        EmptyQuery(message = stringResource(id = R.string.no_users_found))
    } else {
        LazyColumn (
            state = columnState,
            modifier = modifier
                .fillMaxWidth()
                .padding(Paddings.medium)
        ) {
            items(users) {
                UserCard(
                    user = it,
                    onClick = { toUserDetail(it) },
                    modifier = Modifier
                        .padding(Paddings.small / 2)
                )
            }
        }
    }
}

@Composable
fun UserCard(
    user: DomainUser,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface (
        onClick = onClick,
        color = Color.LightGray,
        shape = RoundedCornerShape(5.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.small)
        ) {
            Text(text = user.id.toString())
            Text(text = user.email)
            Text(text = "${user.name} ${user.surname}")
        }
    }
}
