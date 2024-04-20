package com.example.vhsrental.data.repositories

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.vhsrental.data.VHSRentalDB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val db: VHSRentalDB
) {
}
