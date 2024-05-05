package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vhsrental.data.exceptions.LoginException
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.repositories.UserRepository
import com.example.vhsrental.ui.screens.spine.Tab
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

sealed class UpdateAccountActions {
    data object OnDisplayDataRequest : UpdateAccountActions()
    data object DismissAlert : UpdateAccountActions()
    data class OnNameUpdate(val update: String) : UpdateAccountActions()
    data class OnSurnameUpdate(val update: String) : UpdateAccountActions()
    data class OnEmailUpdate(val update: String) : UpdateAccountActions()
    data class OnPasswordUpdate(val update: String) : UpdateAccountActions()
    data class OnPasswordRepeatUpdate(val update: String) : UpdateAccountActions()
    data class OnOldPasswordUpdate(val update: String) : UpdateAccountActions()
    data object OnConfirmUpdateData : UpdateAccountActions()
    data object OnConfirmUpdatePassword : UpdateAccountActions()
    data class OnSwitchTab(val newTab: Tab) : UpdateAccountActions()
    data object OnLogOut : UpdateAccountActions()
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
        var successfulRegister: Boolean = false,
    ) : LoginUiState()

    data class LoggedIn (
        var user: DomainUser,
        var newName: String = "",
        var newSurname: String = "",
        var newEmail: String = "",
        var newPassword: String = "",
        var newPasswordRepeat: String = "",
        var oldPassword: String = "",
        var updateDataMessage: String = "",
        var updatePasswordMessage: String = "",
        var selectedTab: Tab = Tab.Movies,
        var isError: Boolean = false,
        var errorMessage: String = "",
        var successfulUpdate: Boolean = false,
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

    fun emitActionLoggedIn(action: UpdateAccountActions) {
        when (action) {
            is UpdateAccountActions.OnEmailUpdate ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(newEmail = action.update) }
            is UpdateAccountActions.OnNameUpdate ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(newName = action.update) }
            is UpdateAccountActions.OnOldPasswordUpdate ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(oldPassword = action.update) }
            is UpdateAccountActions.OnPasswordRepeatUpdate ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(newPasswordRepeat = action.update) }
            is UpdateAccountActions.OnPasswordUpdate ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(newPassword = action.update) }
            is UpdateAccountActions.OnSurnameUpdate ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(newSurname = action.update) }
            is UpdateAccountActions.OnConfirmUpdatePassword -> updatePassword()
            is UpdateAccountActions.OnConfirmUpdateData -> updateAccountData()
            is UpdateAccountActions.OnSwitchTab ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(selectedTab = action.newTab) }
            is UpdateAccountActions.OnLogOut -> _uiStateFlow.update { LoginUiState.Login() }
            is UpdateAccountActions.OnDisplayDataRequest ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(
                    selectedTab = Tab.AccountDetail,
                ) }
            is UpdateAccountActions.DismissAlert ->
                _uiStateFlow.update { (it as LoginUiState.LoggedIn).copy(successfulUpdate = false) }
        }
    }

    private fun updatePassword() {
        try {
            _userRepository.updatePassword(uiStateFlow.value as LoginUiState.LoggedIn)
            _uiStateFlow.update { LoginUiState.LoggedIn(
                user = _userRepository.getUserById(
                    (uiStateFlow.value as LoginUiState.LoggedIn).user.id),
                selectedTab = Tab.AccountDetail,
                successfulUpdate = true
            ) }
        } catch (e: Exception) {
            _uiStateFlow.update {(uiStateFlow.value as LoginUiState.LoggedIn).copy(
                isError = true,
                errorMessage = e.message ?: "Error",
                oldPassword = ""
            ) }
        }
    }

    private fun updateAccountData() {
        try {
            _userRepository.updateAccountData(uiStateFlow.value as LoginUiState.LoggedIn)
            _uiStateFlow.update { LoginUiState.LoggedIn(
                user = _userRepository.getUserById(
                    (uiStateFlow.value as LoginUiState.LoggedIn).user.id),
                selectedTab = Tab.AccountDetail,
                successfulUpdate = true
            ) }
        } catch (e: Exception) {
            _uiStateFlow.update { (uiStateFlow.value as LoginUiState.LoggedIn).copy(
                isError = true,
                errorMessage = e.message ?: "Error",
                newEmail = ""
            ) }
        }
    }

    private fun switchScreen() {
        when (uiStateFlow.value) {
            is LoginUiState.Login -> _uiStateFlow.update { LoginUiState.Register() }
            is LoginUiState.Register -> _uiStateFlow.update { LoginUiState.Login() }
            else -> Unit
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