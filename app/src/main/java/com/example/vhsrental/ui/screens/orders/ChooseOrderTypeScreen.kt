package com.example.vhsrental.ui.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.vhsrental.ui.theme.Paddings

@Composable
fun ChooseOrderTypeScreen(
    isReservation: () -> Unit,
    isNewOrder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonModifier = Modifier
        .padding(Paddings.large)
        .fillMaxWidth()
        .height(200.dp)

    Column (
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxSize()
    ) {
        OrderTypeButton(
            text = "Create From Reservation",
            color = Color.Blue,
            textColor = Color.White,
            onClick = isReservation,
            modifier = buttonModifier
        )
        OrderTypeButton(
            text = "Create New Order",
            color = Color.Red,
            textColor = Color.White,
            onClick = isNewOrder,
            modifier = buttonModifier,
        )
    }
}

@Composable
fun OrderTypeButton (
    text: String,
    color: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface (
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        color = color,
        modifier = modifier
    ) {
        Box (
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
