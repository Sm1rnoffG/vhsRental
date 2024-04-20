package com.example.vhsrental.ui.submodels

import com.example.vhsrental.data.repositories.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesSubModel @Inject constructor(
    private val repository: MovieRepository
) {

}