package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val db: VHSRentalDB
) {
}