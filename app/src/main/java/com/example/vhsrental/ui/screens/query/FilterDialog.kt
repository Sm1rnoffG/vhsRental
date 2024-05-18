package com.example.vhsrental.ui.screens.query

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.Format
import com.example.vhsrental.data.models.Genre
import com.example.vhsrental.data.models.OrderRecord
import com.example.vhsrental.data.models.OrderState
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
fun FilterDialog(
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
                fontSize = 35.sp,
                color = Color.White
            )

            when (currentTab) {
                is Tab.Movies -> MovieFilterOptions(movieVm = movieVm)
                is Tab.Users -> UserFilterOptions(userVm = userVm)
                is Tab.Orders -> OrderFilterOptions(orderVm = orderVm)
                is Tab.MyOrders -> OrderFilterOptions(orderVm = orderVm)
                else -> Unit
            }
        }
    }
}

@Composable
fun MovieFilterOptions(
    movieVm: MoviesViewModel,
    modifier: Modifier = Modifier
) {
    val genreButtons = listOf(
        Pair("Only action movies") { movie: DomainMovie -> movie.genre == Genre.Action },
        Pair("Only comedy movies") { movie: DomainMovie -> movie.genre == Genre.Comedy },
        Pair("Only family movies") { movie: DomainMovie -> movie.genre == Genre.Family },
        Pair("Only romantic movies") { movie: DomainMovie -> movie.genre == Genre.Romantic },
        Pair("Only horror movies") { movie: DomainMovie -> movie.genre == Genre.Action },
    )
    val formatButtons = listOf(
        Pair("Only DVD") { movie: DomainMovie -> movie.format == Format.DVD },
        Pair("Only VHS") { movie: DomainMovie -> movie.format == Format.VHS },
        Pair("Only Blue-Ray") { movie: DomainMovie -> movie.format == Format.BlueRay },
    )
    val ageRatingButtons = listOf(
        Pair("3+") { movie: DomainMovie -> movie.ageRating == 3L },
        Pair("8+") { movie: DomainMovie -> movie.ageRating == 8L },
        Pair("12+") { movie: DomainMovie -> movie.ageRating == 12L },
        Pair("15+") { movie: DomainMovie -> movie.ageRating == 15L },
        Pair("18+") { movie: DomainMovie -> movie.ageRating == 18L },
    )
    val availabilityButtons = listOf(
        Pair("Available") { movie: DomainMovie -> movie.currentlyAvailable > 0 },
        Pair("Not available") { movie: DomainMovie -> movie.currentlyAvailable == 0L },
    )

    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth(0.7f)

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        item {
            Text(
                text = "Genres",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(Paddings.small)
            )
        }
        items(genreButtons) {
            Button(
                onClick = {
                    movieVm.emitAction(MovieActions.OnFilter(it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
        item {
            Text(
                text = "Formats",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(Paddings.small)
            )
        }
        items(formatButtons) {
            Button(
                onClick = {
                    movieVm.emitAction(MovieActions.OnFilter(it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
        item {
            Text(
                text = "Age Ratings",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(Paddings.small)
            )
        }
        items(ageRatingButtons) {
            Button(
                onClick = {
                    movieVm.emitAction(MovieActions.OnFilter(it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
        item {
            Text(
                text = "Availability",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(Paddings.small)
            )
        }
        items(availabilityButtons) {
            Button(
                onClick = {
                    movieVm.emitAction(MovieActions.OnFilter(it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
    }
}

@Composable
fun UserFilterOptions(
    userVm: UsersViewModel,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(count = 1) {
            Text(
                text = "Role",
                color = Color.White,
                fontSize = 20.sp
            )
            Button(
                onClick = {
                    userVm.emitAction(UserActions.OnFilter { user: DomainUser -> user.role == Role.User })
                },
                modifier = buttonModifier
            ) {
                Text(text = "Only users")
            }
            Button(
                onClick = {
                    userVm.emitAction(UserActions.OnFilter { user: DomainUser -> user.role == Role.Employee })
                },
                modifier = buttonModifier
            ) {
                Text(text = "Only employees")
            }
        }
    }
}

@Composable
fun OrderFilterOptions(
    orderVm: OrderViewModel,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier
        .padding(Paddings.small)
        .fillMaxWidth(0.7f)
        .wrapContentHeight()

    val statusButtons = listOf(
        Pair("Only in progress") { record: OrderRecord -> record.order.state == OrderState.InProgress },
        Pair("Only extended") { record: OrderRecord -> record.order.state == OrderState.Extended },
        Pair("Only done") { record: OrderRecord -> record.order.state == OrderState.Done },
        Pair("Only reservations") { record: OrderRecord -> record.order.state == OrderState.Reservation },
        Pair("Only overdue") { record: OrderRecord -> record.order.state == OrderState.OverDue },
    )

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        item {
            Text(
                text = "Availability",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(Paddings.small)
            )
        }
        items(statusButtons) {
            Button(
                onClick = {
                    orderVm.emitAction(OrderActions.OnFilter(it.second))
                },
                modifier = buttonModifier
            ) {
                Text(text = it.first)
            }
        }
    }
}
