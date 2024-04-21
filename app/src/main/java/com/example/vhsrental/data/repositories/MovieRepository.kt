package com.example.vhsrental.data.repositories

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.models.ADataModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val db: VHSRentalDB
) : IRepository {
    override fun sellectAll(): List<ADataModel> {
        TODO("Not yet implemented")
    }

    override fun filter(column: String, value: String): List<ADataModel> {
        TODO("Not yet implemented")
    }

    override fun sort(column: String, lowToHigh: Boolean): List<ADataModel> {
        TODO("Not yet implemented")
    }

    override fun search(column: String, value: String): List<ADataModel> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun insert(new: ADataModel) {
        TODO("Not yet implemented")
    }

}
