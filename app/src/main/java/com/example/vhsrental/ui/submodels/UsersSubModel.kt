package com.example.vhsrental.ui.submodels

import com.example.vhsrental.data.repositories.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersSubModel @Inject constructor(
    private val repository: UserRepository
) {
}