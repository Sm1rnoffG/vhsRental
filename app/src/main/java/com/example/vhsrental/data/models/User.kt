package com.example.vhsrental.data.models

data class User (
    override val id: Int,
    var name: String,
    var email: String,
    var password: String
) : ADataModel()
