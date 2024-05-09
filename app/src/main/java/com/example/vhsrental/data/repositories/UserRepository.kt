package com.example.vhsrental.data.repositories

import com.example.vhsrental.data.VHSRentalDB
import com.example.vhsrental.data.exceptions.LoginException
import com.example.vhsrental.data.models.ADomainModel
import com.example.vhsrental.data.models.DBUser
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.viewmodels.LoginUiState
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val db: VHSRentalDB,
) : IRepository {
    override suspend fun selectAll() = db.selectAllUsers().map { it.asDomainModel() }

    override suspend fun delete(id: Long) = db.deleteUser(id)

    override suspend fun insert(new: ADomainModel) {
        val user = new as DomainUser
        db.addUser(user.asDBModel())
    }

    fun updateAccountData(attempt: LoginUiState.LoggedIn) {
        if (attempt.newName.isEmpty() && attempt.newSurname.isEmpty() && attempt.newEmail.isEmpty())
            throw LoginException.NoDataProvidedException()

        if (attempt.newEmail.isNotEmpty()) {
            if (!isEmailValid(attempt.newEmail))
                throw LoginException.InvalidEmailException()
            if (getUsersWithEmail(attempt.newEmail).isNotEmpty())
                throw LoginException.UserAlreadyExistsException()
        }

        val updated = DBUser(
            id = attempt.user.id,
            name = attempt.newName.ifEmpty { attempt.user.name },
            surname = attempt.newSurname.ifEmpty { attempt.user.surname },
            email = attempt.newEmail.ifEmpty { attempt.user.email },
            password = attempt.user.password,
            role = attempt.user.role.ordinal.toLong(),
        )
        db.updateUser(updated)
    }

    fun updatePassword(attempt: LoginUiState.LoggedIn) {
        if (attempt.newPassword.isEmpty() || attempt.newPasswordRepeat.isEmpty() ||
            attempt.oldPassword.isEmpty())
            throw LoginException.NewPasswordNotSetException()

        var password = attempt.user.password
        if (!getHash(attempt.oldPassword.toByteArray()).contentEquals(password))
            throw LoginException.InvalidPasswordException()
        if (attempt.newPassword != attempt.newPasswordRepeat)
            throw LoginException.PasswordDoesNotMatchException()
        password = getHash(attempt.newPassword.toByteArray())

        val updated = DBUser(
            id = attempt.user.id,
            name = attempt.user.name,
            surname = attempt.user.surname,
            email = attempt.user.email,
            password = password,
            role = attempt.user.role.ordinal.toLong(),
        )
        db.updateUser(updated)
    }

    private fun getUsersWithEmail(email: String) =
        db.getUserByEmail(email).map { it.asDomainModel() }

    fun getUserById(id: Long) = db.getUserById(id).asDomainModel()

    suspend fun login(attempt: LoginUiState.Login) : DomainUser {
        if (attempt.email.isEmpty() || attempt.password.isEmpty())
            throw LoginException.EmptyFieldException()

        val users = selectAll()
        val password = getHash(attempt.password.toByteArray())
        val user = users.filter { it.email == attempt.email && it.password.contentEquals(password) }

        if (user.isEmpty()) throw LoginException.WrongNameOrPasswordException()
        return user[0]
    }

    fun promoteUser(user: DomainUser) {
        db.updateUser(user.copy(role = Role.Employee).asDBModel())
    }

    suspend fun register(attempt: LoginUiState.Register) {
        if (attempt.name.isEmpty() || attempt.surname.isEmpty() || attempt.email.isEmpty() ||
            attempt.password.isEmpty() || attempt.passwordRepeat.isEmpty())
                throw LoginException.EmptyFieldException()

        if (!isEmailValid(attempt.email))
            throw LoginException.InvalidEmailException()

        if (attempt.password != attempt.passwordRepeat)
            throw LoginException.PasswordDoesNotMatchException()

        val users = db.selectAllUsers()
        if (users.any { attempt.email == it.email })
            throw LoginException.UserAlreadyExistsException()

        val passwordHash = getHash(attempt.password.toByteArray())
        val id = (users.maxOfOrNull { it.id } ?: 0) + 1

        insert(DomainUser(
            name = attempt.name, surname = attempt.surname, email = attempt.email,
            password = passwordHash, id = id, role = Role.User,
        ))
    }

    private fun getHash(message: ByteArray) =
        MessageDigest.getInstance("SHA-256").digest(message)

    private fun isEmailValid(email: String) =
        Regex("""([^\s@]+)@([^\s@]+)\.([^\s@]+)""").matches(email)
}