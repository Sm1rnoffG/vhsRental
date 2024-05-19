package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.Query
import com.example.vhsrental.data.exceptions.OrderExceptions
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.OrderRecord
import com.example.vhsrental.data.models.OrderState
import com.example.vhsrental.data.repositories.MovieRepository
import com.example.vhsrental.data.repositories.OrderRepository
import com.example.vhsrental.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed class OrderActions {
    data class OnReservationRequest(val movie: DomainMovie, val user: DomainUser) : OrderActions()
    data class OnOrderDetailRequest(val record: OrderRecord) : OrderActions()
    data class OnMyOrderDetailRequest(val record: OrderRecord) : OrderActions()
    data class UpdateSearchValue(val update: String) : OrderActions()
    data class OnSearch(val searchFunction: (OrderRecord) -> String) : OrderActions()
    data class OnFilter(val filterFunction: (OrderRecord) -> Boolean) : OrderActions()
    data class OnSort(val sortFunction: Comparator<OrderRecord>, val flipped: Boolean) : OrderActions()
    data class OnDeleteUser(val userId: Long) : OrderActions()
    data class OnLoadMyList(val user: DomainUser): OrderActions()
    data object OnOrderFinish : OrderActions()
    data object OnLoadList : OrderActions()
    data object OnOrderExtend : OrderActions()
}

data class OrderUiState (
    val orders: Query<OrderRecord>,
    val order: DomainOrder? = null,
    val user: DomainUser? = null,
    val movie: DomainMovie? = null,
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<OrderUiState> =
        MutableStateFlow(OrderUiState(Query(getRecords())))
    val uiStateFlow: StateFlow<OrderUiState>
        get() = _uiStateFlow

    init {
        orderRepository.deleteOld()
    }
    fun emitAction(action: OrderActions) {
        when (action) {
            is OrderActions.OnDeleteUser -> deleteUsersOrders(usersId = action.userId)
            is OrderActions.OnLoadList ->
                _uiStateFlow.update { uiStateFlow.value.copy(orders = Query(getRecords())) }
            is OrderActions.OnLoadMyList ->
                _uiStateFlow.update { uiStateFlow.value.copy(orders = Query(getRecords(action.user))) }
            is OrderActions.OnOrderDetailRequest ->
                _uiStateFlow.update { uiStateFlow.value.copy(order = action.record.order, movie = action.record.movie, user = action.record.user) }
            is OrderActions.OnOrderExtend -> extendOrder()
            is OrderActions.OnOrderFinish -> finishOrder()
            is OrderActions.OnReservationRequest -> reserveMovie(action.movie, action.user)
            is OrderActions.OnMyOrderDetailRequest ->
                _uiStateFlow.update { uiStateFlow.value.copy(order = action.record.order, movie = action.record.movie) }
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

    fun canEmployeeEdit(movie: DomainMovie) =
        getAllOrders().none { it.state != OrderState.Done && it.movie == movie.id }

    fun canUserReserve(movie: DomainMovie, user: DomainUser) =
        getUsersOrders(user.id).none { it.state != OrderState.Done && it.movie == movie.id }

    fun getUsersOrders(userId: Long) = getAllOrders().filter { it.user == userId }

    private fun reload(user: DomainUser? = null) {
        val new = if (user != null) getRecords(user) else getRecords()

        _uiStateFlow.update { uiStateFlow.value.copy(
            orders = Query(new),
            order = orderRepository.getOrderById(it.order?.id ?: throw OrderExceptions.UnexpectedException())
        ) }
    }
    private fun getRecords(user: DomainUser? = null) : List<OrderRecord> {
        return if (user != null) {
            getAllOrders().filter { it.user == user.id }
                .map { order -> OrderRecord(
                    order = order,
                    movie = movieRepository.getMovieById(order.movie),
                    user = user
                ) }
        } else {
            getAllOrders().map { order -> OrderRecord(
                order = order,
                movie = movieRepository.getMovieById(order.movie),
                user = userRepository.getUserById(order.user)
            ) }
        }
    }

    private fun getAllOrders() = orderRepository.selectAll()

    private fun deleteUsersOrders(usersId: Long) =
        orderRepository.deleteUsersOrders(getUsersOrders(usersId))

    private fun reserveMovie(movie: DomainMovie, user: DomainUser) {
        orderRepository.createOrder (
            movieId = movie.id,
            userId = user.id,
            isReservationRequest = true
        )
        reload(user)
    }

    private fun finishOrder() {
        val updatedOrder = uiStateFlow.value.order?.copy(state = OrderState.Done) ?:
            throw OrderExceptions.UnexpectedException()
        orderRepository.insert(updatedOrder)
        reload()
    }

    private fun extendOrder() {
        val updatedOrder = uiStateFlow.value.order?.copy(
            state = OrderState.Extended,
            returnDate = uiStateFlow.value.order?.returnDate?.plusDays(30)
        ) ?: throw OrderExceptions.UnexpectedException()

        orderRepository.insert(updatedOrder)
        reload(userRepository.getUserById(updatedOrder.user))
    }
}