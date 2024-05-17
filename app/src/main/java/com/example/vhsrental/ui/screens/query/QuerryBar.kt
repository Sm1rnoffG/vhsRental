package com.example.vhsrental.ui.screens.query

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.vhsrental.R
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
fun QueryBar(
    movieVm: MoviesViewModel,
    orderVm: OrderViewModel,
    usersVm: UsersViewModel,
    currentUser: DomainUser,
    displayedTab: Tab,
    modifier: Modifier = Modifier,
) {
    val displaySearchDialog = remember { mutableStateOf(false) }
    val displaySortDialog = remember { mutableStateOf(false) }
    val displayFilterDialog = remember { mutableStateOf(false) }
    val buttonModifier = Modifier
        .padding(Paddings.small)

    when {
        displaySearchDialog.value -> SearchDialog(
            movieVm = movieVm,
            orderVm = orderVm,
            userVm = usersVm,
            currentTab = displayedTab,
            currentUser = currentUser,
            onDismiss = { displaySearchDialog.value = !displaySearchDialog.value }
        )
        displaySortDialog.value -> SortDialog(
            movieVm = movieVm,
            orderVm = orderVm,
            userVm = usersVm,
            currentTab = displayedTab,
            currentUser = currentUser,
            onDismiss = { displaySortDialog.value = !displaySortDialog.value }
        )
        displayFilterDialog.value -> FilterDialog(
            movieVm = movieVm,
            orderVm = orderVm,
            userVm = usersVm,
            currentTab = displayedTab,
            onDismiss = { displayFilterDialog.value = !displayFilterDialog.value }
        )
    }
    Surface (
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.small)
        ) {
            IconButton(
                onClick = { displaySearchDialog.value = !displaySearchDialog.value },
                modifier = buttonModifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "search icon"
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
            ) {
                IconButton(
                    onClick = { displaySortDialog.value = !displaySortDialog.value },
                    modifier = buttonModifier
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_sort_24),
                        contentDescription = "sort icon"
                    )
                }
                IconButton(
                    onClick = { displayFilterDialog.value = !displayFilterDialog.value },
                    modifier = buttonModifier
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                        contentDescription = "filter icon"
                    )
                }
                IconButton(onClick = {
                    clearQuery(
                        movieVm = movieVm,
                        orderVm = orderVm,
                        usersVm = usersVm,
                        currentUser = currentUser,
                        displayedTab = displayedTab,
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "clear icon"
                    )
                }
            }
        }
    }
}

fun clearQuery(
    movieVm: MoviesViewModel,
    orderVm: OrderViewModel,
    usersVm: UsersViewModel,
    currentUser: DomainUser,
    displayedTab: Tab,
) {
    when (displayedTab) {
        is Tab.Movies -> movieVm.emitAction(MovieActions.LoadCatalogue)
        is Tab.MyOrders -> orderVm.emitAction(OrderActions.OnLoadMyList(currentUser))
        is Tab.Orders -> orderVm.emitAction(OrderActions.OnLoadList)
        is Tab.Users -> usersVm.emitAction(UserActions.OnUserList(currentUser))
        else -> Unit
    }
}
