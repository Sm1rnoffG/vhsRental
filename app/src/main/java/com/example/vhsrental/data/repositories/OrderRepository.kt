package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val db: VHSRentalDB
) : IRepository {
    override fun sellectAll(): List<ADomainModel> {
        return db.selectAllOrders().map { it.asDomainModel() }
    }

    override fun delete(id: Long) {
        db.deleteOrder(id)
    }

    override fun insert(new: ADomainModel) {
        val order = new as DomainOrder
        db.addOrder(order.asDBModel())
    }
}