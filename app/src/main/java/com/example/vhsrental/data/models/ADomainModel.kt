package com.example.vhsrental.data.models

abstract class ADomainModel {
    abstract val id: Long

    abstract fun asDBModel() : ADBModel
}