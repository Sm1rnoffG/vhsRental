package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.ui.screens.movies.Movie
import com.example.vhsrental.ui.viewmodels.OrderUiState

@Composable
fun OrderDetailScreen(
    state: OrderUiState,
    onCloseOrder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val order = state.order
    val movie = state.movie
    val user = state.user

    val textColor = when (order?.state) {
        OrderState.OverDue -> Color.Red
        OrderState.Done -> Color.LightGray
        else -> Color.Black
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Order id: ${order?.id}",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "Order state: ${order?.state?.name}",
            textAlign = TextAlign.Left,
            color = textColor
        )
        Text(
            text = "Order create date: ${order?.createDate}",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "Order return date: ${order?.returnDate ?: order?.createDate?.plusDays(3)}",
            textAlign = TextAlign.Left,
            color =  textColor
        )
        Text(
            text = "Rented movie: ${movie?.name}",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "Movie format: ${movie?.format?.name}",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "Rented by: ${user?.name} ${user?.surname}",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "Contact: ${user?.email}",
            textAlign = TextAlign.Left,
        )

        Button(onClick = onCloseOrder) {
            Text(text = "Finalize order and mark as returned")
        }
    }
}