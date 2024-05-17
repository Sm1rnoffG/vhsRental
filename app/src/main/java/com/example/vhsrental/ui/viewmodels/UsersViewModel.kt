package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.Query
import com.example.vhsrental.data.User
import com.example.vhsrental.data.exceptions.UserExceptions
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class UserActions {
    data class OnUserList(val currentUser: DomainUser) : UserActions()
    data class OnUserDetail(val user: DomainUser) : UserActions()
    data object OnPromoteUser : UserActions()
    data object OnDeleteUser : UserActions()
    data class UpdateSearchValue(val update: String) : UserActions()
    data class OnSearch(val searchFunction: (DomainUser) -> String) : UserActions()
    data class OnFilter(val filterFunction: (DomainUser) -> Boolean) : UserActions()
    data class OnSort(val sortFunction: Comparator<DomainUser>, val flipped: Boolean) : UserActions()
}

data class UserUiState(
    val users: Query<DomainUser>,
    val displayedUser: DomainUser? = null
)

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<UserUiState> =
        MutableStateFlow(UserUiState(Query(getAllUsers())))
    val uiStateFlow: StateFlow<UserUiState>
        get() = _uiStateFlow

    fun emitAction(action: UserActions) {
        when (action) {
            is UserActions.OnDeleteUser ->
                deleteUser(uiStateFlow.value.displayedUser ?: throw UserExceptions.UnexpectedException())
            is UserActions.OnPromoteUser ->
                promoteUser(uiStateFlow.value.displayedUser ?: throw UserExceptions.UnexpectedException())
            is UserActions.OnUserDetail ->
                _uiStateFlow.update { uiStateFlow.value.copy(displayedUser = action.user) }
            is UserActions.OnUserList ->
                _uiStateFlow.update { uiStateFlow.value.copy(
                    users = Query(getAllUsers().filter { it != action.currentUser })
                ) }
            is UserActions.OnFilter ->
                _uiStateFlow.update { uiStateFlow.value.copy(users = uiStateFlow.value.users.filter(action.filterFunction)) }
            is UserActions.OnSearch ->
                _uiStateFlow.update { uiStateFlow.value.copy(users = uiStateFlow.value.users.search(action.searchFunction)) }
            is UserActions.OnSort ->
                _uiStateFlow.update { uiStateFlow.value.copy(users = uiStateFlow.value.users.sort(action.sortFunction, action.flipped)) }
            is UserActions.UpdateSearchValue ->
                _uiStateFlow.update { uiStateFlow.value.copy(users = uiStateFlow.value.users.updateSearch(action.update)) }
        }
    }

    private fun promoteUser(user: DomainUser) = repository.promoteUser(user)

    private fun deleteUser(user: DomainUser) = repository.delete(user.id)

    fun getAllUsers() : List<DomainUser> = repository.selectAll()
}