package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.models.ADomainModel

interface IRepository {
    suspend fun selectAll() : List<ADomainModel>
    suspend fun delete(id: Long)
    suspend fun insert(new: ADomainModel)
}