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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
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
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(Paddings.large)
        ) {
            Text(
                text = "Filter",
                color = Color.White,
                fontSize = 35.sp
            )

            when (currentTab) {
                is Tab.Movies -> MovieSortOptions(movieVm = movieVm)
                is Tab.Orders -> OrderSortOptions(orderVm = orderVm)
                is Tab.MyOrders -> Unit
                is Tab.Users -> UserSortOptions(userVm = userVm)
                else -> Unit
            }
        }
    }
}

@Composable
fun MovieSortOptions(
    movieVm: MoviesViewModel,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth(0.7f)

    val buttons = listOf<Triple<String, Boolean, Comparator<DomainMovie>>>(
        Triple("By id ascending", false, compareBy { it.id }),
        Triple("By id descending", true, compareBy { it.id }),
        Triple("By name ascending", false, compareBy { it.name }),
        Triple("By name descending", true, compareBy { it.name }),
        Triple("By release year ascending", false, compareBy { it.releaseYear }),
        Triple("By release year descending", true, compareBy { it.releaseYear }),
    )

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
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth(0.7f)

    val buttons = listOf<Triple<String, Boolean, Comparator<DomainOrder>>>(
        Triple("By create date ascending", false, compareBy { it.createDate }),
        Triple("By create data descending", true, compareBy { it.createDate }),
        Triple("By id ascending", false, compareBy { it.id }),
        Triple("By id descending", true, compareBy { it.id }),
    )

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
        .fillMaxWidth(0.7f)

    val buttons = listOf<Triple<String, Boolean, Comparator<DomainUser>>>(
        Triple("By surname ascending", false, compareBy { it.surname }),
        Triple("By surname descending", true, compareBy { it.surname }),
        Triple("By e-mail ascending", false, compareBy { it.email }),
        Triple("By e-mail descending", true, compareBy { it.email }),
        Triple("By id ascending", false, compareBy { it.id }),
        Triple("By id descending", true, compareBy { it.id }),
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