package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.exceptions.OrderExceptions
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderRecord
import com.example.vhsrental.data.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


sealed class CreateOrderActions {
    data class OnUserUpdate(val update: DomainUser) : CreateOrderActions()
    data class OnMovieUpdate(val update: DomainMovie) : CreateOrderActions()
    data class OnCreateOrderFromReservation(val reservation: DomainOrder) : CreateOrderActions()
    data object OnCreateOrder : CreateOrderActions()
    data object OnOrderConfirm : CreateOrderActions()
    data object OnOrderFromReservationConfirm : CreateOrderActions()
    data object OnClearUser : CreateOrderActions()
    data object OnClearMovie : CreateOrderActions()
}

data class CreateOrderUiState (
    val movie: DomainMovie? = null,
    val user: DomainUser? = null,
    val reservation: DomainOrder? = null,
    val canCreate: Boolean = false,
)

@HiltViewModel
class OrderCreatingViewModel @Inject constructor(
    private val repository: OrderRepository,
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(CreateOrderUiState())
    val uiStateFlow: StateFlow<CreateOrderUiState>
        get() = _uiStateFlow

    fun emitAction(action: CreateOrderActions) {
        when (action) {
            is CreateOrderActions.OnCreateOrder ->
                _uiStateFlow.update { CreateOrderUiState() }
            is CreateOrderActions.OnClearMovie ->
                _uiStateFlow.update { uiStateFlow.value.copy(movie = null) }
            is CreateOrderActions.OnClearUser ->
                _uiStateFlow.update { uiStateFlow.value.copy(user = null) }
            is CreateOrderActions.OnMovieUpdate -> {
                _uiStateFlow.update { uiStateFlow.value.copy(movie = action.update) }
                checkCanCreate()
            }
            is CreateOrderActions.OnUserUpdate -> {
                _uiStateFlow.update { uiStateFlow.value.copy(user = action.update) }
                checkCanCreate()
            }
            is CreateOrderActions.OnOrderConfirm -> createOrder()
            is CreateOrderActions.OnCreateOrderFromReservation ->
                _uiStateFlow.update { uiStateFlow.value.copy(reservation = action.reservation) }
            CreateOrderActions.OnOrderFromReservationConfirm ->
                createOrderFromReservation()
        }
    }

    private fun checkCanCreate() {
        val user = uiStateFlow.value.user
        val movie = uiStateFlow.value.movie

        if (user != null && movie != null) {
            _uiStateFlow.update { uiStateFlow.value.copy(
                canCreate = repository.selectAll()
                    .filter { it.user == uiStateFlow.value.user?.id }
                    .none { it.movie == uiStateFlow.value.movie?.id }
            ) }
        } else {
            _uiStateFlow.update { uiStateFlow.value.copy(canCreate = false) }
        }
    }

    private fun createOrderFromReservation() {
        repository.createOrderFromReservation(
            reservation = uiStateFlow.value.reservation ?: throw OrderExceptions.EmptyFieldException()
        )
    }

    private fun createOrder() = repository.createOrder(
        userId = uiStateFlow.value.user?.id ?: throw OrderExceptions.EmptyFieldException(),
        movieId = uiStateFlow.value.movie?.id ?: throw OrderExceptions.EmptyFieldException(),
        isReservationRequest = false
    )
}