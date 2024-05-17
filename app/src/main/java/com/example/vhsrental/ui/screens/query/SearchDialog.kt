package com.example.vhsrental.ui.screens.query

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.ui.navigation.Tab
import com.example.vhsrental.ui.screens.OptionSelectDropdownMenu
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UserActions
import com.example.vhsrental.ui.viewmodels.UsersViewModel

@Composable
fun SearchDialog(
    movieVm: MoviesViewModel,
    orderVm: OrderViewModel,
    userVm: UsersViewModel,
    currentTab: Tab,
    onDismiss: () -> Unit
) {
    val searchContent = when (currentTab) {
        is Tab.Movies -> movieVm.uiStateFlow.collectAsState().value.catalogue.searchValue
        is Tab.Users -> userVm.uiStateFlow.collectAsState().value.users.searchValue
        is Tab.Orders -> orderVm.uiStateFlow.collectAsState().value.orders.searchValue
        else -> ""
    }
    val onValueChange = when (currentTab) {
        is Tab.Movies -> { new: String -> movieVm.emitAction(MovieActions.UpdateSearchValue(new)) }
        is Tab.Users -> { new: String -> userVm.emitAction(UserActions.UpdateSearchValue(new)) }
        is Tab.Orders -> { new: String -> userVm.emitAction(UserActions.UpdateSearchValue(new))}
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
                is Tab.Orders -> Unit // TODO
                is Tab.MyOrders -> UserSearchOptions(userVm = userVm)
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
    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(count = 1) {
            Button(
                onClick = { 
                    movieVm.emitAction(MovieActions.OnSearch { movie: DomainMovie -> movie.name })
                },
                modifier = Modifier
                    .padding(Paddings.small)
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
        .fillMaxWidth(0.7f)
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
