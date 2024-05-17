package com.example.vhsrental.data.models

import java.time.LocalDate

enum class OrderState {
    Reservation,
    InProgress,
    Done,
    OverDue,
    Extended
}

data class OrderRecord(
    val order: DomainOrder,
    val movie: DomainMovie,
    val user: DomainUser,
)

data class DomainOrder (
    override val id: Long,
    var state: OrderState,
    var createDate: LocalDate,
    var returnDate: LocalDate?,
    var user: Long,
    var movie: Long
) : ADomainModel() {

    override fun asDBModel() : DBOrder {
        return DBOrder(
            id, state.ordinal.toLong(), createDate.toString(), returnDate.toString(), user, movie
        )
    }
}
