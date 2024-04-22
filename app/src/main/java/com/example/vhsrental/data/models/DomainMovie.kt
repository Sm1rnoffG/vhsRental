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

data class DomainMovie (
    override val id: Long,
    var name: String,
    var length: Long,
    var availableCopies: Long,
    var currentlyAvailable: Long,
    var format: Format,
    var ageRating: Long,
    var imageUrl: String,
    var imdbUrl: String,
    var releaseYear: Long,
    var description: String,
    var genre: Genre
) : ADomainModel() {

    override fun asDBModel() : DBMovie {
        return DBMovie(
            id, name, length, availableCopies, currentlyAvailable, format.ordinal.toLong(), ageRating,
            imageUrl, imdbUrl, releaseYear, description, genre.ordinal.toLong()
        )
    }
}
