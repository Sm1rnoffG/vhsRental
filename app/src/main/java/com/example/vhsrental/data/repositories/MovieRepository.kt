package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainMovie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val db: VHSRentalDB
) : IRepository {
    override fun sellectAll(): List<DomainMovie> {
        return db.selectAllMovies().map { it.asDomainModel() }
    }

    override fun delete(id: Long) {
        db.deleteMovie(id)
    }

    override fun insert(new: ADomainModel) {
        val movie = new as DomainMovie
        db.addMovie(movie.asDBModel())
    }
}
