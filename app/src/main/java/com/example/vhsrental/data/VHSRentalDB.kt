package com.example.vhsrental.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class VHSRentalDB @Inject constructor(@ActivityContext context: Context) {

    private val driver: SqlDriver

    init {
        driver = AndroidSqliteDriver(
            schema = VHSRental.Schema,
            context = context,
            name = "vhsrental.db"
        )
    }
}