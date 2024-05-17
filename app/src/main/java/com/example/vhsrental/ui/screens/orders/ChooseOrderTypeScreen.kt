package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vhsrental.ui.theme.Paddings

@Composable
fun ChooseOrderTypeScreen(
    isReservation: () -> Unit,
    isNewOrder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column (
        modifier = modifier.fillMaxSize()
    ) {
        Surface (
            onClick = isReservation,
            shape = RoundedCornerShape(5.dp),
            color = Color.Blue,
            modifier = Modifier
                .padding(Paddings.small)
                .fillMaxWidth()
                .fillMaxHeight(0.5F)
        ) {
            Text(text = "Create From Reservation", color = Color.White)
        }
        Surface (
            onClick = isNewOrder,
            shape = RoundedCornerShape(5.dp),
            color = Color.Red,
            modifier = Modifier
                .padding(Paddings.small)
                .fillMaxWidth()
                .fillMaxHeight(0.5F)
        ) {
            Text(text = "Create New Order", color = Color.White)
        }
    }
}