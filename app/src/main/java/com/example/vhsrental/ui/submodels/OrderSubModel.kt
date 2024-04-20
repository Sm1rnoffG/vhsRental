package com.example.vhsrental.ui.submodels

import com.example.vhsrental.data.repositories.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderSubModel @Inject constructor(
    private val repository: OrderRepository
) {
}