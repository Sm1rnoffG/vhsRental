package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderRecord
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.ui.screens.EmptyQuery
import com.example.vhsrental.ui.theme.Paddings

@Composable
fun OrderScreen(
    orderData: List<OrderRecord>,
    modifier: Modifier = Modifier,
    onOrderSelection: (OrderRecord) -> Unit,
) {
    val listState = rememberLazyListState()

    if (orderData.isEmpty()) {
        EmptyQuery(message = "No orders found")
    } else {
        LazyColumn (
            state = listState,
            modifier = modifier
                .fillMaxWidth()
        ) {
            items(orderData) {data ->
                OrderPreview(
                    user = data.user,
                    order = data.order,
                    movie = data.movie,
                    onClick = { onOrderSelection(data) },
                    modifier = Modifier
                        .padding(Paddings.medium)
                )
            }
        }
    }
}

@Composable
fun OrderPreview(
    user: DomainUser?,
    order: DomainOrder,
    movie: DomainMovie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = when (order.state) {
        OrderState.OverDue -> Color.Red
        OrderState.Done -> Color.LightGray
        else -> Color.Black
    }

    Surface (
        onClick = onClick,
        color = Color.LightGray,
        shape = RoundedCornerShape(5.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(Paddings.small)
        ) {
            Text(
                text = "${movie.name} on ${movie.format}",
                fontSize = 20.sp
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(Paddings.small)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Text(
                        text = (user?.name ?: "NaV") + " " + (user?.surname ?: "NaV"),
                        textAlign = TextAlign.Left,
                    )
                    Text(
                        text = user?.email ?: "NaV",
                        textAlign = TextAlign.Left
                    )
                }
                Column {
                    Text(
                        text = order.returnDate.toString(),
                        textAlign = TextAlign.Right,
                        color = textColor,
                    )
                    Text(
                        text = order.state.name,
                        textAlign = TextAlign.Right,
                        color = textColor,
                    )
                }
            }
        }
    }
}
