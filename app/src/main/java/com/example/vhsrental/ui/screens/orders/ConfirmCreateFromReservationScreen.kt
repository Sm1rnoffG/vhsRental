package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vhsrental.R
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.CreateOrderUiState
import java.time.LocalDate

@Composable
fun ConfirmCreateFromReservation(
    reservation: CreateOrderUiState,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = stringResource(id = R.string.res_order_heading),
            fontSize = 35.sp,
        )
        Text(
            text = stringResource(id = R.string.res_order_movie_heading) +
                    reservation.user?.name +
                    reservation.user?.surname,
            fontSize = 20.sp
        )
        Text(
            text = stringResource(id = R.string.res_order_movie_heading) + reservation.movie?.name,
            fontSize = 20.sp
        )
        Spacer(Modifier.size(5.dp))
        Text(
            text = stringResource(id = R.string.res_order_r_date_heading),
            textAlign = TextAlign.Center,
        )
        Text(
            text = LocalDate.now().plusDays(30).toString(),
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onConfirm,
            modifier = Modifier.padding(Paddings.medium)
        ) {
            Text(text = "Create Order")
        }
    }
}