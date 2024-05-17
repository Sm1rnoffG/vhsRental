package com.example.vhsrental.ui.screens.query

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderRecord
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.navigation.Tab
import com.example.vhsrental.ui.screens.OptionSelectDropdownMenu
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderActions
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UserActions
import com.example.vhsrental.ui.viewmodels.UsersViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun SearchDialog(
    movieVm: MoviesViewModel,
    orderVm: OrderViewModel,
    userVm: UsersViewModel,
    currentTab: Tab,
    currentUser: DomainUser,
    onDismiss: () -> Unit
) {
    val searchContent = when (currentTab) {
        is Tab.Movies -> movieVm.uiStateFlow.collectAsState().value.catalogue.searchValue
        is Tab.Users -> userVm.uiStateFlow.collectAsState().value.users.searchValue
        is Tab.Orders -> orderVm.uiStateFlow.collectAsState().value.orders.searchValue
        is Tab.MyOrders -> orderVm.uiStateFlow.collectAsState().value.orders.searchValue
        else -> ""
    }
    val onValueChange = when (currentTab) {
        is Tab.Movies -> { new: String -> movieVm.emitAction(MovieActions.UpdateSearchValue(new)) }
        is Tab.Users -> { new: String -> userVm.emitAction(UserActions.UpdateSearchValue(new)) }
        is Tab.Orders -> { new: String -> orderVm.emitAction(OrderActions.UpdateSearchValue(new)) }
        is Tab.MyOrders -> { new: String -> orderVm.emitAction(OrderActions.UpdateSearchValue(new)) }
        else -> { _ -> Unit }
    }
    
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Search",
                color = Color.White,
                fontSize = 35.sp
            ) 
            TextField(
                value = searchContent,
                onValueChange = onValueChange
            )
            
            when (currentTab) {
                is Tab.Movies -> MovieSearchOptions(movieVm = movieVm)
                is Tab.Users -> UserSearchOptions(userVm = userVm)
                is Tab.Orders -> OrderSearchOptions(orderVm = orderVm, asEmployee = currentUser.role == Role.Employee)
                is Tab.MyOrders -> OrderSearchOptions(orderVm = orderVm, asEmployee = currentUser.role == Role.Employee)
                else -> Unit
            }
        }
    }
}

@Composable
fun MovieSearchOptions(
    movieVm: MoviesViewModel,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth()
        .wrapContentHeight()

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(count = 1) {
            Button(
                onClick = { 
                    movieVm.emitAction(MovieActions.OnSearch { movie: DomainMovie -> movie.name })
                },
                modifier = buttonModifier
            ) {
                Text(text = "In names of movies")
            }
        }
    }
}

@Composable
fun UserSearchOptions(
    userVm: UsersViewModel,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth()
        .wrapContentHeight()
    
    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        items(count = 1) {
            Button(
                onClick = {
                    userVm.emitAction(UserActions.OnSearch { user: DomainUser -> user.name })
                },
                modifier = buttonModifier
            ) {
                Text(text = "In names of users")
            }
            Button(
                onClick = {
                    userVm.emitAction(UserActions.OnSearch { user: DomainUser -> user.surname })
                },
                modifier = buttonModifier
            ) {
                Text(text = "In surnames of users")
            }
            Button(
                onClick = {
                    userVm.emitAction(UserActions.OnSearch { user: DomainUser -> user.email })
                },
                modifier = buttonModifier
            ) {
                Text(text = "In e-mails of users")
            }
        }
    }
}

@Composable
fun OrderSearchOptions(
    orderVm: OrderViewModel,
    asEmployee: Boolean,
    modifier: Modifier = Modifier,
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth()
        .wrapContentHeight()

    val buttons = mutableListOf(
        Pair("In rented movies") { record: OrderRecord -> record.movie.name },
    )
    if (asEmployee) buttons.addAll(listOf(
        Pair("In user names") { record: OrderRecord -> record.movie.name },
        Pair("In user surnames") { record: OrderRecord -> record.movie.name },
        Pair("In user e-mails") { record: OrderRecord -> record.movie.name },
    ))

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(buttons) {
            Button(
                onClick = {
                    orderVm.emitAction(OrderActions.OnSearch(it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
    }
}
