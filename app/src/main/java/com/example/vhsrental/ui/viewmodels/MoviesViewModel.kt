package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

}