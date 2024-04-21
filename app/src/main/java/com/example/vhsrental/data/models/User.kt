package com.example.vhsrental.data.models

enum class Role {
    Employee,
    User
}

data class User (
    override val id: Int,
    var name: String,
    var surname: String,
    var email: String,
    var password: String,
    var role: Role
) : ADataModel()
