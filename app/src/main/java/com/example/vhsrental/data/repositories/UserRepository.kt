package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.exceptions.LoginException
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.viewmodels.LoginUiState
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val db: VHSRentalDB
) : IRepository {
    override suspend fun selectAll(): List<DomainUser> {
        return db.selectAllUsers().map { it.asDomainModel() }
    }

    override suspend fun delete(id: Long) {
        db.deleteUser(id)
    }

    override suspend fun insert(new: ADomainModel) {
        val user = new as DomainUser
        db.addUser(user.asDBModel())
    }

    suspend fun login(attempt: LoginUiState.Login) : DomainUser {
        val users = selectAll()
        val password = getHash(attempt.password.toByteArray())
        val user = users.filter { it.email == attempt.email && it.password.contentEquals(password) }

        if (user.size != 1) throw LoginException.WrongNameOrPasswordException()
        return user[0]
    }

    suspend fun register(attempt: LoginUiState.Register) {
        if (attempt.password != attempt.passwordRepeat)
            throw LoginException.PasswordDoesNotMatchException()

        val users = db.selectAllUsers()
        if (users.any { attempt.email == it.email })
            throw LoginException.UserAlreadyExistsException()

        val passwordHash = getHash(attempt.password.toByteArray())
        val id = (users.maxOfOrNull { it.id } ?: 0) + 1
        insert(DomainUser(
            name = attempt.name, surname = attempt.surname, email = attempt.email,
            password = passwordHash, id = id, role = Role.User
        ))
    }

    private fun getHash(message: ByteArray) : ByteArray {
        return MessageDigest.getInstance("SHA-256").digest(message)
    }
}