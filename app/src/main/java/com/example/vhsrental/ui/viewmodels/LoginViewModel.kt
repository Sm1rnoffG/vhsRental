package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vhsrental.data.exceptions.LoginException
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class LoginActions {
    data class OnLoginPasswordUpdate(val update: String) : LoginActions()
    data class OnLoginEmailUpdate(val update: String) : LoginActions()
    data class OnRegisterNameUpdate(val update: String) : LoginActions()
    data class OnRegisterSurnameUpdate(val update: String) : LoginActions()
    data class OnRegisterPasswordUpdate(val update: String) : LoginActions()
    data class OnRegisterPasswordRepeatUpdate(val update: String) : LoginActions()
    data class OnRegisterEmailUpdate(val update: String) : LoginActions()
    data object OnLoginAttempt : LoginActions()
    data object OnRegisterAttempt : LoginActions()
    data object OnSwitchScreen : LoginActions()
}

sealed class LoginUiState {
    data class Login (
        var email: String = "",
        var password: String = "",
        var loginError: Boolean = false,
        var loginErrorMessage: String = ""
    ) : LoginUiState()

    data class Register (
        var name: String = "",
        var surname: String = "",
        var email: String = "",
        var password: String = "",
        var passwordRepeat: String = "",
        var registerError: Boolean = false,
        var registerErrorMessage: String = "",
        var successfulRegister: Boolean = false
    ) : LoginUiState()

    data class LoggedIn (
        var user: DomainUser
    ) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val _userRepository: UserRepository
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.Login())

    val uiStateFlow: StateFlow<LoginUiState>
        get() = _uiStateFlow

    fun emitAction(action: LoginActions) {
        when (action) {
            is LoginActions.OnLoginEmailUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Login).copy(email = action.update) }
            is LoginActions.OnLoginPasswordUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Login).copy(password = action.update) }
            is LoginActions.OnRegisterNameUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Register).copy(name = action.update) }
            is LoginActions.OnRegisterSurnameUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Register).copy(surname = action.update) }
            is LoginActions.OnRegisterEmailUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Register).copy(email = action.update) }
            is LoginActions.OnRegisterPasswordUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Register).copy(password = action.update) }
            is LoginActions.OnRegisterPasswordRepeatUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Register).copy(passwordRepeat = action.update) }
            is LoginActions.OnLoginAttempt -> login()
            is LoginActions.OnRegisterAttempt -> register()
            is LoginActions.OnSwitchScreen -> switchScreen()
        }
    }

    private fun switchScreen() {
        when (uiStateFlow.value) {
            is LoginUiState.Login -> _uiStateFlow.update { LoginUiState.Register() }
            is LoginUiState.Register -> _uiStateFlow.update { LoginUiState.Login() }
            else -> throw Exception("this should be impossible")
        }
    }

    private fun login() {
        val state = uiStateFlow.value as LoginUiState.Login

        viewModelScope.launch (Dispatchers.IO) {
            try {
                val user = _userRepository.login(state)
                _uiStateFlow.update { LoginUiState.LoggedIn(user) }
            } catch (e: LoginException) {
                _uiStateFlow.update {
                    state.copy(
                        loginError = true,
                        loginErrorMessage = e.message ?: "Error",
                        password = ""
                ) }
            }
        }
    }

    private fun register() {
        val state = uiStateFlow.value as LoginUiState.Register

        viewModelScope.launch (Dispatchers.IO) {
            try {
                _userRepository.register(state)
            } catch (e: LoginException) {
                _uiStateFlow.update {
                    state.copy(
                        registerError = true,
                        registerErrorMessage = e.message ?: "Error"
                ) }
            }
        }
        _uiStateFlow.update {
            (uiStateFlow.value as LoginUiState.Register).copy(successfulRegister = true)
        }
    }
}