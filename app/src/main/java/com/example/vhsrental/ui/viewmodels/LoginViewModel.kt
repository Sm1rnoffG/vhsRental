package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.exceptions.LoginException
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.repositories.UserRepository
import com.example.vhsrental.ui.navigation.Tab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


sealed class LoginActions {
    data class OnPasswordUpdate(val update: String) : LoginActions()
    data class OnEmailUpdate(val update: String) : LoginActions()
    data class OnNameUpdate(val update: String) : LoginActions()
    data class OnSurnameUpdate(val update: String) : LoginActions()
    data class OnPasswordRepeatUpdate(val update: String) : LoginActions()
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
        var name: String = "",
        var surname: String = "",
        var email: String = "",
        var password: String = "",
        var passwordRepeat: String = "",
        var isError: Boolean = false,
        var errorMessage: String = "",
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
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiStateFlow: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.Login())

    val uiStateFlow: StateFlow<LoginUiState>
        get() = _uiStateFlow

    fun emitAction(action: LoginActions) {
        when (action) {
            is LoginActions.OnEmailUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Login).copy(email = action.update) }
            is LoginActions.OnPasswordUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Login).copy(password = action.update) }
            is LoginActions.OnNameUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Login).copy(name = action.update) }
            is LoginActions.OnSurnameUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Login).copy(surname = action.update) }
            is LoginActions.OnPasswordRepeatUpdate ->
                _uiStateFlow.update { (it as LoginUiState.Login).copy(passwordRepeat = action.update) }
            is LoginActions.OnLoginAttempt -> login()
            is LoginActions.OnRegisterAttempt -> register()
            is LoginActions.OnSwitchScreen -> switchScreen()
        }
    }

    fun emitActionLoggedIn(action: UpdateAccountActions) {
        val state = uiStateFlow.value as LoginUiState.LoggedIn

        when (action) {
            is UpdateAccountActions.OnEmailUpdate ->
                _uiStateFlow.update { state.copy(newEmail = action.update) }
            is UpdateAccountActions.OnNameUpdate ->
                _uiStateFlow.update { state.copy(newName = action.update) }
            is UpdateAccountActions.OnOldPasswordUpdate ->
                _uiStateFlow.update { state.copy(oldPassword = action.update) }
            is UpdateAccountActions.OnPasswordRepeatUpdate ->
                _uiStateFlow.update { state.copy(newPasswordRepeat = action.update) }
            is UpdateAccountActions.OnPasswordUpdate ->
                _uiStateFlow.update { state.copy(newPassword = action.update) }
            is UpdateAccountActions.OnSurnameUpdate ->
                _uiStateFlow.update { state.copy(newSurname = action.update) }
            is UpdateAccountActions.OnConfirmUpdatePassword -> updatePassword()
            is UpdateAccountActions.OnConfirmUpdateData -> updateAccountData()
            is UpdateAccountActions.OnSwitchTab ->
                _uiStateFlow.update { state.copy(selectedTab = action.newTab) }
            is UpdateAccountActions.OnLogOut -> _uiStateFlow.update { LoginUiState.Login() }
            is UpdateAccountActions.OnDisplayDataRequest ->
                _uiStateFlow.update { state.copy(selectedTab = Tab.AccountDetail) }
            is UpdateAccountActions.DismissAlert ->
                _uiStateFlow.update { state.copy(successfulUpdate = false) }
        }
    }

    private fun updatePassword() {
        try {
            userRepository.updatePassword(uiStateFlow.value as LoginUiState.LoggedIn)
            _uiStateFlow.update { LoginUiState.LoggedIn(
                user = userRepository.getUserById(
                    (uiStateFlow.value as LoginUiState.LoggedIn).user.id
                ),
                selectedTab = Tab.AccountDetail,
                successfulUpdate = true,
            ) }
        } catch (e: Exception) {
            _uiStateFlow.update {(uiStateFlow.value as LoginUiState.LoggedIn).copy(
                isError = true,
                errorMessage = e.message ?: "Error",
                oldPassword = "",
            ) }
        }
    }

    private fun updateAccountData() {
        try {
            userRepository.updateAccountData(uiStateFlow.value as LoginUiState.LoggedIn)
            _uiStateFlow.update { LoginUiState.LoggedIn(
                user = userRepository.getUserById(
                    (uiStateFlow.value as LoginUiState.LoggedIn).user.id
                ),
                selectedTab = Tab.AccountDetail,
                successfulUpdate = true,
            ) }
        } catch (e: Exception) {
            _uiStateFlow.update { (uiStateFlow.value as LoginUiState.LoggedIn).copy(
                isError = true,
                errorMessage = e.message ?: "Error",
                newEmail = "",
            ) }
        }
    }

    private fun switchScreen() = _uiStateFlow.update { LoginUiState.Login() }

    private fun login() {
        val state = uiStateFlow.value as LoginUiState.Login

        try {
            val user = userRepository.login(state)
            _uiStateFlow.update { LoginUiState.LoggedIn(user) }
        } catch (e: LoginException) {
            _uiStateFlow.update {
                state.copy(
                    isError = true,
                    errorMessage = e.message ?: "Error",
                    password = "",
            ) }
        }
    }

    private fun register() {
        val state = uiStateFlow.value as LoginUiState.Login

        try {
            userRepository.register(state)
            _uiStateFlow.update {
                (uiStateFlow.value as LoginUiState.Login).copy(
                    successfulRegister = true,
                    isError = false,
                )
            }
        } catch (e: LoginException) {
            _uiStateFlow.update {
                state.copy(
                    isError = true,
                    successfulRegister = false,
                    errorMessage = e.message ?: "Error",
            ) }
        }
    }
}