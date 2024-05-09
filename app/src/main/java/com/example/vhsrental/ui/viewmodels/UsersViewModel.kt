package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}

sealed class UserUiState {
    data class UserList(val users: List<DomainUser>) : UserUiState()
    data class UserDetail(val user: DomainUser) : UserUiState()
}

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<UserUiState> =
        MutableStateFlow(UserUiState.UserList(getAllUsers()))
    val uiStateFlow: StateFlow<UserUiState>
        get() = _uiStateFlow

    fun emitAction(action: UserActions) {
        when (action) {
            is UserActions.OnDeleteUser ->
                deleteUser((uiStateFlow.value as UserUiState.UserDetail).user)
            is UserActions.OnPromoteUser ->
                promoteUser((uiStateFlow.value as UserUiState.UserDetail).user)
            is UserActions.OnUserDetail ->
                _uiStateFlow.update { UserUiState.UserDetail(action.user) }
            is UserActions.OnUserList ->
                _uiStateFlow.update { UserUiState.UserList(getAllUsers().filter { it != action.currentUser }) }
        }
    }

    private fun promoteUser(user: DomainUser) {
        repository.promoteUser(user)
    }

    private fun deleteUser(user: DomainUser) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.delete(user.id)
        }
    }

    fun getAllUsers() : List<DomainUser> {
        var users = emptyList<DomainUser>()

        viewModelScope.launch (Dispatchers.IO) {
            users = repository.selectAll()
        }

        return users
    }
}