package com.example.vhsrental.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PaddingSizes(
    val large: Dp,
    val medium: Dp,
    val small: Dp
)


// Define value Paddings which will hold instance of PaddingSizes.
//  Sizes will be:
//  - large: 16 density pixels
//  - medium: 8 density pixels
//  - small: 4 density pixels
val Paddings = PaddingSizes(
    large = 16.dp,
    medium = 8.dp,
    small = 4.dp
)
