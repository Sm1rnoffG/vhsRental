package com.example.vhsrental.data.models

import java.util.Date

sealed class OrderState {
    data object Reservation
}

data class Order (
    val id: Int,
    var state: OrderState,
    var createDate: Date,
    var returnDate: Date?,
    var user: User,
    var movie: Movie
)