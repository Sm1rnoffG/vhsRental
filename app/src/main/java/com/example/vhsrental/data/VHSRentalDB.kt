package com.example.vhsrental.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.vhsrental.data.exceptions.MovieExceptions
import com.example.vhsrental.data.models.DBMovie
import com.example.vhsrental.data.models.DBOrder
import com.example.vhsrental.data.models.DBUser
import com.example.vhsrental.data.models.DEFAULT_USER
import com.example.vhsrental.data.models.Role
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VHSRentalDB @Inject constructor(context: Context) {

    private val driver: SqlDriver = AndroidSqliteDriver(
        schema = VHSRental.Schema,
        context = context,
        name = "vhsrental.db"
    )

    init {
        // context.deleteDatabase("vhsrental.db")
        addUser(
            DBUser(
                0, "main", "admin", "admin@gmail.com",
                MessageDigest.getInstance("SHA-256").digest("admin".toByteArray()),
                Role.Employee.ordinal.toLong()
            )
        )
    }

    fun selectAllMovies() : List<DBMovie> = VHSRental(driver).movieQueries
            .selectAll()
            .executeAsList()
            .map {
                DBMovie(
                    it.id, it.name, it.length, it.available_copies, it.currently_available,
                    it.format, it.age_rating, it.image_url, it.imdb_url, it.release_year,
                    it.description, it.genre
                )
            }

    fun getMovieById(id: Long) = VHSRental(driver).movieQueries
        .getMovie(id)
        .executeAsOneOrNull()
        ?.let {
            DBMovie(
                it.id, it.name, it.length, it.available_copies, it.currently_available,
                it.format, it.age_rating, it.image_url, it.imdb_url, it.release_year,
                it.description, it.genre
            )
        } ?: throw MovieExceptions.NoMovieFoundException()

    fun deleteMovie(id: Long) = VHSRental(driver).movieQueries.deleteMovie(id)

    fun addMovie(movie: DBMovie) = VHSRental(driver).movieQueries.addMovie(
            movie.id, movie.name, movie.length, movie.availableCopies, movie.currentlyAvailable,
            movie.format, movie.ageRating, movie.imageUrl, movie.imdbUrl, movie.releaseYear,
            movie.description, movie.genre
    )

    fun selectAllUsers() : List<DBUser> = VHSRental(driver).userQueries
            .selectAll()
            .executeAsList()
            .map {
                DBUser(
                    it.id, it.name, it.surname, it.email, it.password, it.role
                )
            }

    fun getUserById(id: Long) : DBUser = VHSRental(driver).userQueries
            .getUser(id)
            .executeAsOneOrNull()
            ?.let {
                DBUser(it.id, it.name, it.surname, it.email, it.password, it.role)
        } ?: DEFAULT_USER.asDBModel()

    fun getUserByEmail(email: String) : List<DBUser> = VHSRental(driver).userQueries
            .getUserByEmail(email)
            .executeAsList()
            .map { DBUser(it.id, it.name, it.surname, it.email, it.password, it.role) }

    fun deleteUser(id: Long) = VHSRental(driver).userQueries.deleteUser(id)

    fun addUser(user: DBUser) = VHSRental(driver).userQueries.addUser(
            user.id, user.name, user.surname, user.email, user.password, user.role
        )

    fun selectAllOrders() : List<DBOrder> = VHSRental(driver).orderQueries
            .selectAll()
            .executeAsList()
            .map {
                DBOrder(
                    it.id, it.state, it.create_date, it.return_date, it.user_id, it.movie_id
                )
            }

    fun addOrder(order: DBOrder) = VHSRental(driver).orderQueries.createOrder(
            order.id, order.state, order.createDate, order.returnDate, order.userId, order.movieId
        )

    fun deleteOrder(id: Long) = VHSRental(driver).orderQueries.deleteOrder(id)

    fun updateUser(user: DBUser) = VHSRental(driver).userQueries
            .updateAccount(
                user.id,
                user.name,
                user.surname,
                user.email,
                user.password,
                user.role
            )

    fun deleteMultipleOrders(orders: List<DBOrder>) {
        VHSRental(driver).userQueries.transaction {
            orders.forEach {
                deleteOrder(it.id)
            }
        }
    }

    fun getOrderById(id: Long) = VHSRental(driver).orderQueries.getOrder(id).executeAsOne().let {
        DBOrder(
            it.id, it.state, it.create_date, it.return_date, it.user_id, it.movie_id
        )
    }
}