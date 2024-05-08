package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.Format
import com.example.vhsrental.data.models.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val db: VHSRentalDB
) : IRepository {

    override suspend fun selectAll(): List<DomainMovie> {
        return db.selectAllMovies().map { it.asDomainModel() }
    }

    override suspend fun delete(id: Long) {
        db.deleteMovie(id)
    }

    override suspend fun insert(new: ADomainModel) {
        val movie = new as DomainMovie
        db.addMovie(movie.asDBModel())
    }
}
