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
    override suspend fun selectAll(): List<ADomainModel> {
        return db.selectAllOrders().map { it.asDomainModel() }
    }

    override suspend fun delete(id: Long) {
        db.deleteOrder(id)
    }

    override suspend fun insert(new: ADomainModel) {
        val order = new as DomainOrder
        db.addOrder(order.asDBModel())
    }
}