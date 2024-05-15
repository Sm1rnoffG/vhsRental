package com.example.vhsrental.data.exceptions

sealed class OrderExceptions(message: String) : Exception(message) {
    class EmptyFieldException : OrderExceptions(MESSAGE) {

        companion object {
            const val MESSAGE: String = "Field was left empty during order creation which should" +
                    "not be possible"
        }
    }

    class UnexpectedException : OrderExceptions(message) {
        companion object{
            const val message: String = "This should not be possible"
        }
    }
}