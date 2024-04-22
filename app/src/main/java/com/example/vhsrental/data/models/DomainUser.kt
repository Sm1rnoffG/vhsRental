package com.example.vhsrental.data.models

enum class Role {
    Employee,
    User
}

data class DomainUser (
    override val id: Long,
    var name: String,
    var surname: String,
    var email: String,
    var password: ByteArray,
    var role: Role
) : ADomainModel() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DomainUser

        if (id != other.id) return false
        if (name != other.name) return false
        if (surname != other.surname) return false
        if (email != other.email) return false
        if (!password.contentEquals(other.password)) return false
        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + role.hashCode()
        return result.toInt()
    }

    override fun asDBModel(): DBUser {
        return DBUser(
            id, name, surname, email, password, role.ordinal.toLong()
        )
    }
}
