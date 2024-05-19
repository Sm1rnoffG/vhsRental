package com.example.vhsrental.ui.screens.myorders

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.vhsrental.R
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.ui.screens.movies.Poster
import com.example.vhsrental.ui.screens.users.FinalWarning
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.OrderActions
import java.time.LocalDate

@Composable
fun MyOrderDetailScreen(
    movie: DomainMovie?,
    order: DomainOrder?,
    onMyOrderAction: (OrderActions, DomainMovie?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        items (count = 1) {
            Poster(
                url = movie?.imageUrl ?: "",
                isPreview = false,
                modifier = Modifier
                    .padding(Paddings.small)
            )
            Text(
                text = movie?.name ?: "",
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                modifier = Modifier
                    .padding(Paddings.small)
            )
            Text(
                text = order?.state?.name ?: "",
                textAlign = TextAlign.Center,
                color = if (order?.state == OrderState.OverDue) Color.Red else Color.Black,
                modifier = Modifier
                    .padding(Paddings.small)
            )
            Text(
                text = "${stringResource(id = R.string.order_from_heading)} ${order?.createDate}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(Paddings.small)
            )
            Text(
                text = "${stringResource(id = R.string.order_till_heading)} ${
                    order?.returnDate ?: order?.createDate?.plusDays(3)}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(Paddings.small)
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
            MyOrderActionButton(order, { action -> onMyOrderAction(action, movie) })
        }
    }
}

@Composable
fun MyOrderActionButton(
    order: DomainOrder?,
    onMyOrderAction: (OrderActions) -> Unit,
    modifier: Modifier = Modifier
) {
    var displayDialog by remember { mutableStateOf(false) }
    val action: OrderActions
    val text: String
    val dialogText: String

    when (order?.state) {
        OrderState.Reservation -> { 
            action = OrderActions.OnOrderFinish
            text = stringResource(id = R.string.order_action_res_button)
            dialogText = stringResource(id = R.string.confirm_cancel_res_dialog)
        }
        OrderState.InProgress -> { 
            action = OrderActions.OnOrderExtend
            text = stringResource(id = R.string.order_action_ex_button)
            dialogText = stringResource(id = R.string.confirm_extend_dialog)
        }
        else -> return
    }

    when {
        displayDialog -> FinalWarning(
            heading = text,
            text = dialogText,
            onConfirm = {
                onMyOrderAction(action)
                displayDialog = false
            },
            onCancel = { displayDialog = false }
        )
    }

    Button(
        onClick = { displayDialog = true },
        modifier = modifier.wrapContentSize()
    ) {
        Text(text = text)
    }
}