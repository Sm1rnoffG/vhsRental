package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val db: VHSRentalDB
) {
}