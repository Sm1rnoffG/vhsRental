package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.ui.viewmodels.OrderActions
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val db: VHSRentalDB
) : IRepository {
    override suspend fun selectAll(): List<DomainOrder> {
        return db.selectAllOrders().map { it.asDomainModel() }
    }

    override suspend fun delete(id: Long) {
        db.deleteOrder(id)
    }

    override suspend fun insert(new: ADomainModel) {
        val order = new as DomainOrder
        db.addOrder(order.asDBModel())
    }

    suspend fun createOrder(
        userId: Long,
        movieId: Long,
        isReservationRequest: Boolean,
    ) {
        val creationDate = LocalDate.now()
        val returnDate = creationDate.plusDays(if (isReservationRequest) 3 else 30)
        val state = if (isReservationRequest) OrderState.Reservation else OrderState.InProgress

        insert(DomainOrder(
            id = selectAll().maxOf { it.id } + 1,
            user = userId,
            movie = movieId,
            createDate = creationDate,
            returnDate = returnDate,
            state = state
        ))
    }
}