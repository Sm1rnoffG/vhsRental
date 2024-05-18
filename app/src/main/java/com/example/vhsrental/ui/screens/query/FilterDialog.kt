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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vhsrental.R
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
                text = stringResource(id = R.string.filter_heading),
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
        Pair(stringResource(id = R.string.filter_movie_genre_action)) { movie: DomainMovie -> movie.genre == Genre.Action },
        Pair(stringResource(id = R.string.filter_movie_genre_comedy)) { movie: DomainMovie -> movie.genre == Genre.Comedy },
        Pair(stringResource(id = R.string.filter_movie_genre_family)) { movie: DomainMovie -> movie.genre == Genre.Family },
        Pair(stringResource(id = R.string.filter_movie_genre_romantic)) { movie: DomainMovie -> movie.genre == Genre.Romantic },
        Pair(stringResource(id = R.string.filter_movie_genre_horror)) { movie: DomainMovie -> movie.genre == Genre.Action },
    )
    val formatButtons = listOf(
        Pair(stringResource(id = R.string.filter_movie_format_dvd)) { movie: DomainMovie -> movie.format == Format.DVD },
        Pair(stringResource(id = R.string.filter_movie_format_vhs)) { movie: DomainMovie -> movie.format == Format.VHS },
        Pair(stringResource(id = R.string.filter_movie_format_br)) { movie: DomainMovie -> movie.format == Format.BlueRay },
    )
    val ageRatingButtons = listOf(
        Pair(stringResource(id = R.string.filter_movie_age_3)) { movie: DomainMovie -> movie.ageRating == 3L },
        Pair(stringResource(id = R.string.filter_movie_age_8)) { movie: DomainMovie -> movie.ageRating == 8L },
        Pair(stringResource(id = R.string.filter_movie_age_12)) { movie: DomainMovie -> movie.ageRating == 12L },
        Pair(stringResource(id = R.string.filter_movie_age_15)) { movie: DomainMovie -> movie.ageRating == 15L },
        Pair(stringResource(id = R.string.filter_movie_age_18)) { movie: DomainMovie -> movie.ageRating == 18L },
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
                text = stringResource(id = R.string.filter_movie_format_heading),
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
                text = stringResource(id = R.string.filter_movie_format_heading),
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
                text = stringResource(id = R.string.filter_movie_age_heading),
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
                text = stringResource(id = R.string.filter_movie_availability_heading),
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
                text = stringResource(id = R.string.filter_user_role_heading),
                color = Color.White,
                fontSize = 20.sp
            )
            Button(
                onClick = {
                    userVm.emitAction(UserActions.OnFilter { user: DomainUser -> user.role == Role.User })
                },
                modifier = buttonModifier
            ) {
                Text(text = stringResource(id = R.string.filter_user_role_user))
            }
            Button(
                onClick = {
                    userVm.emitAction(UserActions.OnFilter { user: DomainUser -> user.role == Role.Employee })
                },
                modifier = buttonModifier
            ) {
                Text(text = stringResource(id = R.string.filter_user_role_user))
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
        Pair(stringResource(id = R.string.filter_order_progress)) { record: OrderRecord -> record.order.state == OrderState.InProgress },
        Pair(stringResource(id = R.string.filter_order_extended)) { record: OrderRecord -> record.order.state == OrderState.Extended },
        Pair(stringResource(id = R.string.filter_order_done)) { record: OrderRecord -> record.order.state == OrderState.Done },
        Pair(stringResource(id = R.string.filter_order_reservations)) { record: OrderRecord -> record.order.state == OrderState.Reservation },
        Pair(stringResource(id = R.string.filter_order_overdue)) { record: OrderRecord -> record.order.state == OrderState.OverDue },
    )

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        item {
            Text(
                text = stringResource(id = R.string.filter_order_status_heading),
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
