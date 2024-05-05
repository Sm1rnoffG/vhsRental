package com.example.vhsrental.data.exceptions

sealed class LoginException(message: String) : Exception(message) {
    class WrongNameOrPasswordException : LoginException(MESSAGE) {

        companion object {
            const val MESSAGE: String = "Invalid name or password"
        }
    }

    class PasswordDoesNotMatchException : LoginException(MESSAGE) {

        companion object {
            const val MESSAGE: String = "Passwords do not match"
        }
    }

    class UserAlreadyExistsException : LoginException(MESSAGE) {

        companion object {
            const val MESSAGE: String = "User with this e-mail already exists"
        }
    }

    class EmptyFieldException : LoginException(MESSAGE) {

        companion object {
            const val MESSAGE: String = "Please fill in all fields"
        }
    }

    class InvalidEmailException : LoginException(MESSAGE) {

        companion object {
            const val MESSAGE: String = "Please fill in valid e-mail"
        }
    }

    class InvalidPasswordException : LoginException(MESSAGE) {

        companion object {
            const val MESSAGE: String = "Wrong password"
        }
    }

    class NewPasswordNotSetException : LoginException(MESSAGE) {

        companion object {
            const val MESSAGE: String = "You need to fill all fields to change password"
        }
    }

    class NoDataProvidedException : LoginException(MESSAGE) {

        companion object {
            const val MESSAGE: String = "Please provide the information you want to change"
        }
    }
}