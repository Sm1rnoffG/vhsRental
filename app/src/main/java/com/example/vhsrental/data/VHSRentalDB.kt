package com.example.vhsrental.data

import android.content.Context
import app.cash.sqldelight.Query
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.vhsrental.data.models.DBMovie
import com.example.vhsrental.data.models.DBOrder
import com.example.vhsrental.data.models.DBUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VHSRentalDB @Inject constructor(context: Context) {

    private val driver: SqlDriver = AndroidSqliteDriver(
        schema = VHSRental.Schema,
        context = context,
        name = "vhsrental.db"
    )

    fun selectAllMovies() : List<DBMovie> {
        return VHSRental(driver).movieQueries
            .selectAll()
            .executeAsList()
            .map {
                DBMovie(
                    it.id, it.name, it.length, it.available_copies, it.currently_available,
                    it.format, it.age_rating, it.image_url, it.imdb_url, it.release_year,
                    it.description, it.genre
                )
            }
    }

    fun deleteMovie(id: Long) {
        VHSRental(driver).movieQueries.deleteMovie(id)
    }

    fun addMovie(movie: DBMovie) {
        VHSRental(driver).movieQueries.addMovie(
            movie.id, movie.name, movie.length, movie.availableCopies, movie.currentlyAvailable,
            movie.format, movie.ageRating, movie.imageUrl, movie.imdbUrl, movie.releaseYear,
            movie.description, movie.genre
        )
    }

    fun selectAllUsers() : List<DBUser> {
        return VHSRental(driver).userQueries
            .selectAll()
            .executeAsList()
            .map {
                DBUser(
                    it.id, it.name, it.surname, it.email, it.password, it.role
                )
            }
    }

    fun getUserById(id: Long) : DBUser {
        return VHSRental(driver).userQueries
            .getUser(id)
            .executeAsList()[0].let {
                DBUser(it.id, it.name, it.surname, it.email, it.password, it.role)
        }
    }

    fun getUserByEmail(email: String) : List<DBUser> {
        return VHSRental(driver).userQueries
            .getUserByEmail(email)
            .executeAsList()
            .map { DBUser(it.id, it.name, it.surname, it.email, it.password, it.role) }
    }

    fun deleteUser(id: Long) {
        VHSRental(driver).userQueries.deleteUser(id)
    }

    fun addUser(user: DBUser) {
        VHSRental(driver).userQueries.addUser(
            user.id, user.name, user.surname, user.email, user.password, user.role
        )
    }

    fun selectAllOrders() : List<DBOrder> {
        return VHSRental(driver).orderQueries
            .selectAll()
            .executeAsList()
            .map {
                DBOrder(
                    it.id, it.state, it.create_date, it.return_date, it.user_id, it.movie_id
                )
            }
    }

    fun selectUsersOrders(id: Long) : Query<RentalOrder> {
        return VHSRental(driver).orderQueries.selectUsersOrders(id)
    }

    fun addOrder(order: DBOrder) {
        VHSRental(driver).orderQueries.createOrder(
            order.id, order.state, order.createDate, order.returnDate, order.userId, order.movieId
        )
    }

    fun deleteOrder(id: Long) {
        VHSRental(driver).orderQueries.deleteOrder(id)
    }

    fun updateUser(user: DBUser) {
        VHSRental(driver).userQueries
            .updateAccount(
                user.id,
                user.name,
                user.surname,
                user.email,
                user.password,
                user.role
            )
    }
}