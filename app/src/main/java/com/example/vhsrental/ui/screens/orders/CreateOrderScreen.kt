package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vhsrental.ui.screens.Dot
import com.example.vhsrental.ui.viewmodels.CreateOrderUiState
import com.example.vhsrental.ui.viewmodels.OrderUiState
import java.time.LocalDate

@Composable
fun CreateOrderScreen(
    state: CreateOrderUiState,
    modifier: Modifier = Modifier,
    onMovieSelection: () -> Unit = {},
    onMovieClear: () -> Unit = {},
    onUserSelection: () -> Unit = {},
    onUserClear: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Column (
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = "Create an order:",
            textAlign = TextAlign.Center,
        )

        if (state.movie == null) {
            Button(onClick = onMovieSelection ) {
                Text(text = "Choose user")
            }
        } else {
            OrderField(
                left = state.movie.name,
                right = state.movie.format.name,
                onClear = onMovieClear,
            )
        }

        if (state.user == null) {
            Button(onClick = onUserSelection) {
                Text(text = "Choose movie")
            }
        } else {
            OrderField(
                left = "${state.user.name} ${state.user.surname}",
                right = state.user.email,
                onClear = onUserClear,
            )
        }

        Text(
            text = "Due date for the order will be set to:",
            textAlign = TextAlign.Center,
        )
        Text(
            text = LocalDate.now().plusDays(30).toString(),
            textAlign = TextAlign.Center,
        )

        Button(
            onClick = onConfirm,
            enabled = state.user != null && state.movie != null,
        ) {
            Text(text = "Create order")
        }
    }
}

@Composable
fun OrderField(
    left: String,
    right: String,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(text = left)
        Dot()
        Text(text = right)

        Surface (
            shape = CircleShape,
            color = Color.Red,
            onClick = onClear
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "clear button",
                modifier = Modifier
                    .padding(5.dp)
                    .size(30.dp)
            )
        }
    }
}
