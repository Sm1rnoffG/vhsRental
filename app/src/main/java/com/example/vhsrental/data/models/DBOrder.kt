package com.example.vhsrental.data.models

import java.time.LocalDate

data class DBOrder (
    override val id: Long,
    val state: Long,
    val createDate: String,
    val returnDate: String?,
    val userId: Long,
    val movieId: Long
) : ADBModel() {

    override fun asDomainModel() : DomainOrder {
        return DomainOrder(
            id, OrderState.entries[state.toInt()], LocalDate.parse(createDate),
            LocalDate.parse(returnDate) ?: null, userId, movieId
        )
    }
}
