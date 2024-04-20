package com.example.vhsrental.ui

import androidx.lifecycle.ViewModel
import com.example.vhsrental.ui.submodels.MoviesSubModel
import com.example.vhsrental.ui.submodels.OrderSubModel
import com.example.vhsrental.ui.submodels.UsersSubModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val moviesSubModel: MoviesSubModel,
    private val orderSubModel: OrderSubModel,
    private val usersSubModel: UsersSubModel
) : ViewModel() {

}