package com.example.vhsrental.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.Query
import com.example.vhsrental.data.exceptions.OrderExceptions
import com.example.vhsrental.data.models.DEFAULT_MOVIE
import com.example.vhsrental.data.models.DEFAULT_USER
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.data.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed class OrderActions {
    data class OnReservationRequest(val movie: DomainMovie, val user: DomainUser) : OrderActions()
    data class OnOrderDetailRequest(val order: DomainOrder, val movie: DomainMovie, val user: DomainUser) : OrderActions()
    data class OnMyOrderDetailRequest(val order: DomainOrder, val movie: DomainMovie) : OrderActions()
    data class UpdateSearchValue(val update: String) : OrderActions()
    data class OnSearch(val searchFunction: (DomainOrder) -> String) : OrderActions()
    data class OnFilter(val filterFunction: (DomainOrder) -> Boolean) : OrderActions()
    data class OnSort(val sortFunction: Comparator<DomainOrder>, val flipped: Boolean) : OrderActions()
    data class OnDeleteUser(val userId: Long) : OrderActions()
    data class OnLoadMyList(val userId: Long): OrderActions()
    data object OnOrderFinish : OrderActions()
    data object OnLoadList : OrderActions()
    data object OnOrderExtend: OrderActions()
}

data class OrderUiState (
    val orders: Query<DomainOrder>,
    val order: DomainOrder? = null,
    val user: DomainUser? = null,
    val movie: DomainMovie? = null,
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<OrderUiState> =
        MutableStateFlow(OrderUiState(Query(getAllOrders())))
    val uiStateFlow: StateFlow<OrderUiState>
        get() = _uiStateFlow

    init {
        repository.deleteOld()
    }
    fun emitAction(action: OrderActions) {
        when (action) {
            is OrderActions.OnDeleteUser -> deleteUsersOrders(usersId = action.userId)
            is OrderActions.OnLoadList ->
                _uiStateFlow.update { uiStateFlow.value.copy(orders = Query(getAllOrders())) }
            is OrderActions.OnLoadMyList ->
                _uiStateFlow.update { uiStateFlow.value.copy(
                    orders = Query(getAllOrders().filter { it.user != action.userId })
                ) }
            is OrderActions.OnOrderDetailRequest ->
                _uiStateFlow.update { uiStateFlow.value.copy(order = action.order, movie = action.movie, user = action.user) }
            is OrderActions.OnOrderExtend -> extendOrder()
            is OrderActions.OnOrderFinish -> finishOrder()
            is OrderActions.OnReservationRequest -> reserveMovie()
            is OrderActions.OnMyOrderDetailRequest ->
                _uiStateFlow.update { uiStateFlow.value.copy(order = action.order, movie = action.movie) }

            is OrderActions.OnFilter ->
                _uiStateFlow.update { uiStateFlow.value.copy(orders = uiStateFlow.value.orders.filter(action.filterFunction)) }
            is OrderActions.OnSearch ->
                _uiStateFlow.update { uiStateFlow.value.copy(orders = uiStateFlow.value.orders.search(action.searchFunction)) }
            is OrderActions.OnSort ->
                _uiStateFlow.update { uiStateFlow.value.copy(orders = uiStateFlow.value.orders.sort(action.sortFunction, action.flipped)) }
            is OrderActions.UpdateSearchValue ->
                _uiStateFlow.update { uiStateFlow.value.copy(orders = uiStateFlow.value.orders.updateSearch(action.update)) }
        }
    }

    fun getOrderData(users: List<DomainUser>, movies: List<DomainMovie>) =
        uiStateFlow.value.orders.list
            .map {
                Triple(
                    it,
                    movies.find { movie -> movie.id == it.movie } ?: DEFAULT_MOVIE,
                    users.find { user -> user.id == it.user } ?: DEFAULT_USER
                )
            }

    fun canEmployeeEdit(movie: DomainMovie) = getAllOrders().any { it.movie == movie.id }

    fun canUserReserve(movie: DomainMovie, user: DomainUser) =
        getUsersOrders(user.id).none { it.movie == movie.id }

    private fun getAllOrders() : List<DomainOrder> = repository.selectAll()

    private fun deleteUsersOrders(usersId: Long) {
        val orders = getAllOrders().filter { it.user == usersId }
        repository.deleteUsersOrders(orders)
    }

    fun getUsersOrders(userId: Long) = getAllOrders().filter { it.user == userId }

    private fun reserveMovie() {
        try {
            repository.createOrder(
                movieId = uiStateFlow.value.movie?.id ?: throw OrderExceptions.EmptyFieldException(),
                userId = uiStateFlow.value.user?.id ?: throw OrderExceptions.EmptyFieldException(),
                isReservationRequest = true
            )
        } catch (e: Exception) {
            Log.d(null, e.message ?: "Error")
        }
    }

    private fun finishOrder() {
        val updatedOrder = uiStateFlow.value.order?.copy(state = OrderState.Done) ?:
            throw OrderExceptions.UnexpectedException()
        repository.insert(updatedOrder)
    }

    private fun extendOrder() {
        val updatedOrder = uiStateFlow.value.order?.copy(
            state = OrderState.Extended,
            returnDate = uiStateFlow.value.order?.returnDate?.plusDays(30)
        ) ?: throw OrderExceptions.UnexpectedException()

        repository.insert(updatedOrder)

    }
}