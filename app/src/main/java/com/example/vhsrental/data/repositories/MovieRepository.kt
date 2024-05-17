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

    override fun selectAll(): List<DomainMovie> = db.selectAllMovies().map { it.asDomainModel() }

    override fun delete(id: Long) = db.deleteMovie(id)

    override fun insert(new: ADomainModel) {
        val id = try {
            selectAll().maxOf { it.id } + 1
        } catch (e: NoSuchElementException) {
            0
        }
        val movie = (new as DomainMovie).copy(id = id)
        db.addMovie(movie.asDBModel())
    }

    fun getMovieById(id: Long) = db.getMovieById(id).asDomainModel()

    fun updateMovie(updated: DomainMovie) = db.addMovie(updated.asDBModel())
}
