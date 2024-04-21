package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.models.ADataModel

interface IRepository {
    fun sellectAll() : List<ADataModel>
    fun filter(column: String, value: String) : List<ADataModel>
    fun sort(column: String, lowToHigh: Boolean) : List<ADataModel>
    fun search(column: String, value: String) : List<ADataModel>
    fun delete(id: Int)
    fun insert(new: ADataModel)
}