package com.example.vhsrental.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vhsrental.ui.theme.Paddings

@Composable
fun Dot() {
    Surface (
        shape = CircleShape,
        color = Color.Black,
        modifier = Modifier
            .size(10.dp)
            .padding(5.dp)
    ) {}
}

// From https://alexzh.com/jetpack-compose-dropdownmenu/
@Composable
fun <T> OptionSelectDropdownMenu(
    displaySelected: String,
    options: List<Pair<String, T>>,
    onSelection: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Surface(
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(2.dp),
            border = BorderStroke(width = 2.dp, color = Color.DarkGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (displaySelected == "null") "" else displaySelected,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(Paddings.medium)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.first) },
                    onClick = { onSelection(it.second); expanded = false }
                )
            }
        }
    }
}

@Composable
fun EmptyQuery(
    message: String,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "info",
            tint = Color.LightGray
        )
        Text(
            text = message,
            color = Color.LightGray
        )
    }
}
