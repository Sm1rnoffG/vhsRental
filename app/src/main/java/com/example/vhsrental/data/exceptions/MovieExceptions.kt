package com.example.vhsrental.data.exceptions

sealed class MovieExceptions(message: String) : Exception(message) {
    class NotEditingException : MovieExceptions(message) {

        companion object {
            const val message: String = "Ui state is creating new movie"
        }
    }
    class NotCreatingNewException : MovieExceptions(message) {

        companion object {
            const val message: String = "Ui state is editing existing movie"
        }
    }
    class NotEnoughDataExceptions : MovieExceptions(message) {
        companion object {
            const val message: String = "Not enough data to create new movie"
        }
    }
    class NoMovieFoundException : MovieExceptions(message) {
        companion object{
            const val message: String = "Movie assigned to this order does not exist anymore"
        }
    }
    class UnexpectedException : MovieExceptions(message) {
        companion object{
            const val message: String = "This should not be possible"
        }
    }
}