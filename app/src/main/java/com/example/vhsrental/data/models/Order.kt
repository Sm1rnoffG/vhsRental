package com.example.vhsrental.data.models

import java.util.Date

enum class OrderState {
    Reservation,
    InProgress,
    Done
}

data class Order (
    override val id: Int,
    var state: OrderState,
    var createDate: Date,
    var returnDate: Date?,
    var user: User,
    var movie: Movie
) : ADataModel()