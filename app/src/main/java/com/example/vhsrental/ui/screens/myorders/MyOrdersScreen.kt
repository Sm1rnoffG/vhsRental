package com.example.vhsrental.ui.screens.myorders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.ui.screens.QuerryBar
import com.example.vhsrental.ui.screens.movies.Poster
import java.time.LocalDate

@Composable
fun MyOrders(
    myOrders: List<Pair<DomainOrder, DomainMovie>>,
    toOrderMyOrderDetail: (Pair<DomainOrder, DomainMovie>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        // QuerryBar()
        LazyColumn (
            modifier = Modifier.fillMaxWidth()
        ) {
            myOrders.forEach {
                item {
                    Surface(onClick = { toOrderMyOrderDetail(it) }){
                        MyOrderCard(it)
                    }
                }
            }
        }
    }
}

@Composable
fun MyOrderCard(orderPair: Pair<DomainOrder, DomainMovie>, modifier: Modifier = Modifier) {
    val order = orderPair.first
    val movie = orderPair.second

    Row (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Poster(url = movie.imageUrl, isPreview = true)
        MyOrderPreview(movie.name, order.state,
            order.returnDate ?: order.createDate.plusDays(3))
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
        modifier = modifier
    ) {
        Text(text = movieName, textAlign = TextAlign.Right)
        Text(
            text = orderState.name,
            textAlign = TextAlign.Right,
            color = textColor
        )
        Text(
            text = "till ${returnDate.dayOfMonth}. ${returnDate.month}. ${returnDate.year}",
            textAlign = TextAlign.Right,
            color = textColor
        )
    }
}
