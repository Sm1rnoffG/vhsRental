package com.example.vhsrental.ui.viewmodels

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vhsrental.data.exceptions.OrderExeptions
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.data.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class OrderActions {
    data class OnUserUpdate(val update: DomainUser) : OrderActions()
    data class OnMovieUpdate(val update: DomainMovie) : OrderActions()
    data class OnReservationRequest(val movie: DomainMovie, val user: DomainUser) : OrderActions()
    data class OnOrderDetailRequest(val order: DomainOrder) : OrderActions()
    data object OnCreateOrder : OrderActions()
    data object OnOrderFinish : OrderActions()
    data object OnOrderConfirm : OrderActions()
    data object OnClearUser : OrderActions()
    data object OnClearMovie : OrderActions()
    data object OnLoadList : OrderActions()
    data object OnOrderExtend: OrderActions()
}

sealed class OrderUiState {
    data class OrderList(val orders: List<DomainOrder>) : OrderUiState()
    data class OrderCreation(
        var user: DomainUser? = null,
        var movie: DomainMovie? = null
    ) : OrderUiState()
    data class OrderDetail(val order: DomainOrder) : OrderUiState()
}

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<OrderUiState> =
        MutableStateFlow(OrderUiState.OrderList(getAllOrders()))
    val uiStateFlow: StateFlow<OrderUiState>
        get() = _uiStateFlow

    fun emitOrderAction(action: OrderActions) {
        when (action) {
            is OrderActions.OnClearMovie ->
                _uiStateFlow.update { (uiStateFlow.value as OrderUiState.OrderCreation).copy(movie = null) }
            is OrderActions.OnClearUser ->
                _uiStateFlow.update { (uiStateFlow.value as OrderUiState.OrderCreation).copy(user = null) }
            is OrderActions.OnLoadList ->
                _uiStateFlow.update { OrderUiState.OrderList(getAllOrders()) }
            is OrderActions.OnMovieUpdate ->
                _uiStateFlow.update { (uiStateFlow.value as OrderUiState.OrderCreation).copy(movie = action.update) }
            is OrderActions.OnOrderConfirm -> addRecord(isReservationRequest = false)
            is OrderActions.OnUserUpdate ->
                _uiStateFlow.update { (uiStateFlow.value as OrderUiState.OrderCreation).copy(user = action.update) }
            is OrderActions.OnReservationRequest -> addRecord(isReservationRequest = true)
            is OrderActions.OnOrderDetailRequest ->
                _uiStateFlow.update { OrderUiState.OrderDetail(action.order) }
            is OrderActions.OnOrderFinish -> finishOrder()
            is OrderActions.OnOrderExtend -> extendOrder()
            is OrderActions.OnCreateOrder ->
                _uiStateFlow.update { OrderUiState.OrderCreation() }
        }
    }

    private fun getAllOrders() : List<DomainOrder> {
        var orders: List<DomainOrder> = emptyList()

        viewModelScope.launch (Dispatchers.IO) {
            orders = repository.selectAll()
        }

        return orders
    }

    private fun addRecord(isReservationRequest: Boolean) {
        val state = uiStateFlow.value as OrderUiState.OrderCreation

        try {
            viewModelScope.launch (Dispatchers.IO) {
                repository.createOrder(
                    movieId = state.movie?.id ?: throw OrderExeptions.EmptyFieldException(),
                    userId = state.user?.id ?: throw OrderExeptions.EmptyFieldException(),
                    isReservationRequest = isReservationRequest
                )
            }
        } catch (e: Exception) {
            Log.d(null, e.message ?: "Error")
        }
    }

    private fun finishOrder() {
        val uiState = uiStateFlow.value as OrderUiState.OrderDetail
        val updatedOrder = uiState.order.copy(state = OrderState.Done)

        viewModelScope.launch (Dispatchers.IO) {
            repository.insert(updatedOrder)
        }
    }

    private fun extendOrder() {
        val uiState = uiStateFlow.value as OrderUiState.OrderDetail
        val updatedOrder = uiState.order.copy(
            state = OrderState.Extended,
            returnDate = uiState.order.returnDate?.plusDays(30)
        )

        viewModelScope.launch (Dispatchers.IO) {
            repository.insert(updatedOrder)
        }
    }
}