package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val db: VHSRentalDB
) : IRepository {
    override fun sellectAll(): List<DomainUser> {
        return db.selectAllUsers().map { it.asDomainModel() }
    }

    override fun delete(id: Long) {
        db.deleteUser(id)
    }

    override fun insert(new: ADomainModel) {
        val user = new as DomainUser
        db.addUser(user.asDBModel())
    }
}