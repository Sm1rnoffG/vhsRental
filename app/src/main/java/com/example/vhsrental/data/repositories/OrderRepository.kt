package com.example.vhsrental.data.repositories

import android.util.Log
import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.OrderState
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val db: VHSRentalDB
) : IRepository {
    override fun selectAll(): List<DomainOrder> {
        return db.selectAllOrders().map { it.asDomainModel() }
    }

    override fun delete(id: Long) {
        db.deleteOrder(id)
    }

    override fun insert(new: ADomainModel) {
        val order = new as DomainOrder
        db.addOrder(order.asDBModel())
    }

    fun deleteOld() {
        db.deleteMultipleOrders(
            db.selectAllOrders()
                .map { it.asDomainModel() }
                .filter {
                    it.state == OrderState.Reservation &&
                    it.createDate.plusDays(3).isBefore(LocalDate.now())
                }
                .map { it.asDBModel() }
        )
    }

    fun deleteUsersOrders(orders: List<DomainOrder>) {
        db.deleteMultipleOrders(orders.map { it.asDBModel() })
    }

    fun createOrder(
        userId: Long,
        movieId: Long,
        isReservationRequest: Boolean,
    ) {
        val creationDate = LocalDate.now()
        val returnDate = creationDate.plusDays(if (isReservationRequest) 3 else 30)
        val state = if (isReservationRequest) OrderState.Reservation else OrderState.InProgress
        val id = try {
            selectAll().maxOf { it.id } + 1
        } catch (e: NoSuchElementException) { 0 }

        insert(DomainOrder(
            id = id,
            user = userId,
            movie = movieId,
            createDate = creationDate,
            returnDate = returnDate,
            state = state
        ))
    }

    fun getOrderById(id: Long) = db.getOrderById(id).asDomainModel()
}