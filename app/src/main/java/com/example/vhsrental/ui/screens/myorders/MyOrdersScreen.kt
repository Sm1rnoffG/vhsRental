package com.example.vhsrental.ui.screens.myorders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.OrderRecord
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.ui.screens.movies.Poster
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.OrderUiState
import java.time.LocalDate

@Composable
fun MyOrdersScreen(
    myOrders: List<OrderRecord>,
    toOrderMyOrderDetail: (OrderRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxWidth()
        ) {
            items(myOrders) {
                MyOrderCard(
                    record = it,
                    onClick = { toOrderMyOrderDetail(it) },
                    modifier = Modifier
                        .padding(Paddings.small)
                )
            }
        }
    }
}

@Composable
fun MyOrderCard(
    record: OrderRecord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val order = record.order
    val movie = record.movie

    Surface (
        shape = RoundedCornerShape(5.dp),
        onClick = onClick,
        color = Color.LightGray,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Poster(
                url = movie.imageUrl,
                isPreview = true,
                modifier = Modifier
                    .padding(Paddings.small)
            )
            MyOrderPreview(
                movieName = movie.name,
                orderState = order.state,
                returnDate = order.returnDate ?: order.createDate.plusDays(3),
                modifier = Modifier
                    .padding(Paddings.small)
            )
        }
    }
}

@Composable
fun MyOrderPreview(
    movieName: String,
    orderState: OrderState,
    returnDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val textColor = when (orderState) {
        OrderState.OverDue -> Color.Red
        OrderState.Done -> Color.LightGray
        else -> Color.Black
    }

    Column (
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth()
    ) {
        Text(
            text = movieName,
            textAlign = TextAlign.Right,
            fontSize = 35.sp,
            modifier = Modifier.padding(Paddings.small)
        )
        Text(
            text = orderState.name,
            textAlign = TextAlign.Right,
            color = textColor,
            modifier = Modifier.padding(Paddings.small)
        )
        Text(
            text = "Till $returnDate",
            textAlign = TextAlign.Right,
            color = textColor,
            modifier = Modifier.padding(Paddings.small)
        )
    }
}
