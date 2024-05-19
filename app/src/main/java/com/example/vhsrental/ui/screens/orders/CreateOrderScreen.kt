package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vhsrental.R
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.ui.screens.Dot
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.CreateOrderUiState
import java.time.LocalDate

@Composable
fun CreateOrderScreen(
    state: CreateOrderUiState,
    modifier: Modifier = Modifier,
    onMovieSelection: () -> Unit = {},
    onMovieClear: () -> Unit = {},
    onUserSelection: () -> Unit = {},
    onUserClear: () -> Unit = {},
    onConfirm: (DomainMovie?) -> Unit = {}
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Text(
            text = stringResource(id = R.string.create_order_heading),
            textAlign = TextAlign.Center,
            fontSize = 35.sp
        )

        if (state.movie == null) {
            Button(onClick = onMovieSelection ) {
                Text(text = stringResource(id = R.string.choose_movie_heading))
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
                Text(text = stringResource(id = R.string.choose_user_heading))
            }
        } else {
            OrderField(
                left = "${state.user.name} ${state.user.surname}",
                right = state.user.email,
                onClear = onUserClear,
            )
        }

        Text(
            text = stringResource(id = R.string.create_due_date_heading),
            textAlign = TextAlign.Center,
        )
        Text(
            text = LocalDate.now().plusDays(30).toString(),
            textAlign = TextAlign.Center,
        )

        Button(
            onClick = { onConfirm(state.movie) },
            enabled = state.canCreate,
            modifier = Modifier
                .padding(Paddings.small)
        ) {
            Text(text = stringResource(id = R.string.create_order_button))
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
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = left,
            fontSize = 20.sp
        )
        Dot()
        Text(
            text = right,
            fontSize = 20.sp
        )

        Surface (
            shape = CircleShape,
            color = Color.Red,
            onClick = onClear,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(id = R.string.clear_selection),
                modifier = Modifier
                    .padding(5.dp)
                    .size(20.dp)
            )
        }
    }
}
