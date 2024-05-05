package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class MyOrderActions {
    data class Extend(val order: DomainOrder) : MyOrderActions()
    data class Cancel(val order: DomainOrder) : MyOrderActions()
}

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {
    init {
        // TODO update repo on load
    }
}