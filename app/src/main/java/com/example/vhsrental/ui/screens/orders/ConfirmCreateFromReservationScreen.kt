package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.vhsrental.ui.viewmodels.CreateOrderUiState
import java.time.LocalDate

@Composable
fun ConfirmCreateFromReservation(
    reservation: CreateOrderUiState,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "User: ${reservation.user?.name}"
        )
        Text(
            text = "Movie: ${reservation.movie?.name}"
        )
        Text(
            text = "Due date for the order will be set to:",
            textAlign = TextAlign.Center,
        )
        Text(
            text = LocalDate.now().plusDays(30).toString(),
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onConfirm
        ) {
            Text(text = "Create Order")
        }
    }
}