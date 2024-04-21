package com.example.vhsrental.data.models


enum class Format {
    DVD,
    VHS,
    BlueRay
}

enum class Genre {
    Action,
    Romantic,
    Horror,
    Family,
    Comedy
}

data class Movie (
    override val id: Int,
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
) : ADataModel()
