package com.example.vhsrental.ui.screens.myorders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.ui.screens.movies.Poster
import com.example.vhsrental.ui.viewmodels.OrderActions

@Composable
fun MyOrderDetailScreen(
    order: DomainOrder,
    movie: DomainMovie,
    onMyOrderAction: (OrderActions) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Poster(url = movie.imageUrl, isPreview = false)
        Text(
            text = movie.name,
            textAlign = TextAlign.Center
        )
        Text(
            text = order.state.name,
            textAlign = TextAlign.Center,
            color = if (order.state == OrderState.OverDue) Color.Red else Color.Black
        )
        Text(
            text = "From: ${order.createDate}",
            textAlign = TextAlign.Center
        )
        Text(
            text = "Till: ${order.returnDate ?: order.createDate.plusDays(3)}",
            textAlign = TextAlign.Center
        )
        MyOrderActionButton(order, onMyOrderAction)
    }
}

@Composable
fun MyOrderActionButton(
    order: DomainOrder,
    onMyOrderAction: (OrderActions) -> Unit,
    modifier: Modifier = Modifier
) {
    val action: OrderActions
    val text: String
    when (order.state) {
        OrderState.Reservation -> { action = OrderActions.OnOrderFinish; text = "Cancel Reservation" }
        OrderState.InProgress -> { action = OrderActions.OnOrderExtend; text = "Extend order"}
        else -> return
    }

    Button(
        onClick = { onMyOrderAction(action) },
        modifier = modifier.wrapContentSize()
    ) {
        Text(text = text)
    }
}