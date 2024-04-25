package com.example.vhsrental.data.exceptions

sealed class LoginException(message: String) : Exception(message) {
    class WrongNameOrPasswordException : LoginException(message) {

        companion object {
            const val message: String = "Invalid name or password"
        }
    }

    class PasswordDoesNotMatchException : LoginException(message) {

        companion object {
            const val message: String = "Invalid name or password"
        }
    }

    class UserAlreadyExistsException : LoginException(message) {

        companion object {
            const val message: String = "Invalid name or password"
        }
    }
}