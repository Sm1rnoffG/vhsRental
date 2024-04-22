package com.example.vhsrental.data.models

data class DBMovie (
    override val id: Long,
    val name: String,
    val length: Long,
    val availableCopies: Long,
    val currentlyAvailable: Long,
    val format: Long,
    val ageRating: Long,
    val imageUrl: String,
    val imdbUrl: String,
    val releaseYear: Long,
    val description: String,
    val genre: Long
) : ADBModel() {

    override fun asDomainModel() : DomainMovie {
        return DomainMovie(
            id, name, length, availableCopies, currentlyAvailable, Format.entries[format.toInt()],
            ageRating, imageUrl, imdbUrl, releaseYear, description, Genre.entries[genre.toInt()]
        )
    }
}