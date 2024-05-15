package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.exceptions.UserExceptions
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

data class UserUiState(
    val users: List<DomainUser>,
    val displayedUser: DomainUser? = null
)

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<UserUiState> =
        MutableStateFlow(UserUiState(getAllUsers()))
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
                    users = getAllUsers().filter { it != action.currentUser
                }) }
        }
    }

    private fun promoteUser(user: DomainUser) = repository.promoteUser(user)

    private fun deleteUser(user: DomainUser) = repository.delete(user.id)

    fun getAllUsers() : List<DomainUser> = repository.selectAll()
}