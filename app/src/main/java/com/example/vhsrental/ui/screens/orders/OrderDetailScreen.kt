package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.R
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.OrderUiState
import java.time.LocalDate

@Composable
fun OrderDetailScreen(
    state: OrderUiState,
    onCloseOrder: (DomainMovie?) -> Unit,
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "${stringResource(id = R.string.order_id_heading)} ${order?.id}",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "${stringResource(id = R.string.order_state_heading)} ${order?.state?.name}",
            textAlign = TextAlign.Left,
            color = textColor
        )
        Text(
            text = "${stringResource(id = R.string.order_c_date_heading)} ${order?.createDate}",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "${stringResource(id = R.string.order_r_date_heading)} ${order?.returnDate ?: order?.createDate?.plusDays(3)}",
            textAlign = TextAlign.Left,
            color =  textColor
        )
        if (order?.state != OrderState.Done) {
            val remainingDays = order?.returnDate?.toEpochDay()?.minus(LocalDate.now().toEpochDay()) ?:
            order?.createDate?.plusDays(3)?.toEpochDay()?.minus(LocalDate.now().toEpochDay())

            Text(
                text = "${stringResource(id = R.string.remaining_days)} $remainingDays",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(Paddings.small)
            )
        }
        Text(
            text = "${stringResource(id = R.string.order_movie_heading)} ${movie?.name} (${movie?.id})",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "${stringResource(id = R.string.order_movie_format_heading)} ${movie?.format?.name}",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "${stringResource(id = R.string.order_user_heading)} ${user?.name} ${user?.surname} (${user?.id})",
            textAlign = TextAlign.Left,
        )
        Text(
            text = "${stringResource(id = R.string.order_user_contact_heading)} ${user?.email}",
            textAlign = TextAlign.Left,
        )

        Button(
            onClick = { onCloseOrder(movie) },
            enabled = order?.state != OrderState.Done
        ) {
            Text(text = stringResource(id = R.string.finalize_order_button))
        }
    }
}