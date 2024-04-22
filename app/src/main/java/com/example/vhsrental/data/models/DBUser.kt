package com.example.vhsrental.data.models

data class DBUser (
    override val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val password: ByteArray,
    val role: Long
) : ADBModel() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DBUser

        if (id != other.id) return false
        if (name != other.name) return false
        if (surname != other.surname) return false
        if (email != other.email) return false
        if (!password.contentEquals(other.password)) return false
        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + role.hashCode()
        return result
    }

    override fun asDomainModel() : DomainUser {
        return DomainUser(
            id, name, surname, email, password, Role.entries[role.toInt()]
        )
    }
}
