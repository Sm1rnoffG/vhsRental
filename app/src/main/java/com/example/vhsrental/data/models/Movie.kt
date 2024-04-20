package com.example.vhsrental.data.models


sealed class Format {
    data object DVD
    data object VHS
    data object BlueRay
}

sealed class Genre {
    data object Action
    data object Romantic
    data object Horror
    data object Family
    data object Comedy
}

data class Movie (
    val id: Int,
    var name: String,
    var length: Int,
    var availableCopies: Int,
    var currentlyAvailable: Int,
    var format: Format,
    var ageRating: Int,
    var imageUrl: String,
    var imdbUrl: String,
    var releaseYear: Int,
    var description: String,
    var genre: Genre
)
