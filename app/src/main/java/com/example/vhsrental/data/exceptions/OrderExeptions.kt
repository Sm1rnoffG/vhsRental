package com.example.vhsrental.data.exceptions

class OrderExeptions(message: String) : Exception(message) {
    class EmptyFieldException : MovieExceptions(MESSAGE) {

        companion object {
            const val MESSAGE: String = "Field was left empty during order creation which should" +
                    "not be possible"
        }
    }
}