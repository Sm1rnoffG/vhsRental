package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.models.ADomainModel

interface IRepository {
    fun sellectAll() : List<ADomainModel>
    fun delete(id: Long)
    fun insert(new: ADomainModel)
}