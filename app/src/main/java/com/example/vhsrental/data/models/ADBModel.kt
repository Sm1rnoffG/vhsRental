package com.example.vhsrental.data.models

abstract class ADBModel {
    abstract val id: Long

    abstract fun asDomainModel() : ADomainModel
}