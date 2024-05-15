package com.example.vhsrental.data.exceptions

sealed class UserExceptions(message: String) : Exception(message) {
    class UnexpectedException : UserExceptions(MESSAGE) {
        companion object {
            const val MESSAGE: String = "Attempt to modify empty user which should not happen"
        }
    }
}