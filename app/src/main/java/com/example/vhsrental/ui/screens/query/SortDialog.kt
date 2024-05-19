package com.example.vhsrental.ui.screens.query

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vhsrental.R
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderRecord
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.navigation.Tab
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderActions
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UserActions
import com.example.vhsrental.ui.viewmodels.UsersViewModel

@Composable
fun SortDialog(
    movieVm: MoviesViewModel,
    orderVm: OrderViewModel,
    userVm: UsersViewModel,
    currentTab: Tab,
    currentUser: DomainUser,
    onDismiss: () -> Unit
) {
    val asEmployee = currentUser.role == Role.Employee

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(Paddings.large)
        ) {
            Text(
                text = stringResource(id = R.string.sort_heading),
                color = Color.White,
                fontSize = 35.sp
            )

            when (currentTab) {
                is Tab.Movies -> MovieSortOptions(movieVm, asEmployee)
                is Tab.Orders -> OrderSortOptions(orderVm, asEmployee)
                is Tab.MyOrders -> OrderSortOptions(orderVm, asEmployee)
                is Tab.Users -> UserSortOptions(userVm)
                else -> Unit
            }
        }
    }
}

@Composable
fun MovieSortOptions(
    movieVm: MoviesViewModel,
    asEmployee: Boolean,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth()

    val buttons = mutableListOf<Triple<String, Boolean, Comparator<DomainMovie>>>(
        Triple(stringResource(id = R.string.sort_movie_name_asc), false, compareBy { it.name }),
        Triple(stringResource(id = R.string.sort_movie_name_desc), true, compareBy { it.name }),
        Triple(stringResource(id = R.string.sort_movie_year_asc), false, compareBy { it.releaseYear }),
        Triple(stringResource(id = R.string.sort_movie_year_desc), true, compareBy { it.releaseYear }),
    )
    if (asEmployee) buttons.addAll(listOf(
        Triple(stringResource(id = R.string.sort_movie_id_asc), false, compareBy { it.id }),
        Triple(stringResource(id = R.string.sort_movie_id_desc), true, compareBy { it.id }),
    ))

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(buttons) {
            Button(
                onClick = {
                    movieVm.emitAction(MovieActions.OnSort(it.third, it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
    }
}

@Composable
fun OrderSortOptions(
    orderVm: OrderViewModel,
    asEmployee: Boolean,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth()

    val buttons = mutableListOf<Triple<String, Boolean, Comparator<OrderRecord>>>(
        Triple(stringResource(id = R.string.sort_order_c_date_asc), false, compareBy { it.order.createDate }),
        Triple(stringResource(id = R.string.sort_order_c_date_desc), true, compareBy { it.order.createDate }),
        Triple(stringResource(id = R.string.sort_order_movie_name_asc), false, compareBy { it.movie.name }),
        Triple(stringResource(id = R.string.sort_order_movie_name_desc), true, compareBy { it.movie.name }),
    )
    if (asEmployee) buttons.addAll(listOf(
        Triple(stringResource(id = R.string.sort_order_id_asc), false, compareBy { it.order.id }),
        Triple(stringResource(id = R.string.sort_order_id_desc), true, compareBy { it.order.id }),
        Triple(stringResource(id = R.string.sort_order_email_asc), false, compareBy { it.user.email }),
        Triple(stringResource(id = R.string.sort_order_email_desc), true, compareBy { it.user.email })
    ))

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(buttons) {
            Button(
                onClick = {
                    orderVm.emitAction(OrderActions.OnSort(it.third, it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
    }
}

@Composable
fun UserSortOptions(
    userVm: UsersViewModel,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth()

    val buttons = listOf<Triple<String, Boolean, Comparator<DomainUser>>>(
        Triple(stringResource(id = R.string.sort_user_surname_asc), false, compareBy { it.surname }),
        Triple(stringResource(id = R.string.sort_user_surname_desc), true, compareBy { it.surname }),
        Triple(stringResource(id = R.string.sort_user_email_asc), false, compareBy { it.email }),
        Triple(stringResource(id = R.string.sort_user_email_desc), true, compareBy { it.email }),
        Triple(stringResource(id = R.string.sort_user_id_asc), false, compareBy { it.id }),
        Triple(stringResource(id = R.string.sort_user_id_desc), true, compareBy { it.id }),
    )

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(buttons) {
            Button(
                onClick = {
                    userVm.emitAction(UserActions.OnSort(it.third, it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
    }
}