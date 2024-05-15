package com.example.vhsrental.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.vhsrental.ui.navigation.AppSpine
import com.example.vhsrental.ui.theme.VhsRentalTheme
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MovieEditingViewModel
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderCreatingViewModel
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {
    private val moviesViewModel: MoviesViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()
    private val ordersViewModel: OrderViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val movieEditingViewModel: MovieEditingViewModel by viewModels()
    private val orderCreatingViewModel: OrderCreatingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VhsRentalTheme {
                AppSpine(
                    movieVm = moviesViewModel,
                    editMovieVm = movieEditingViewModel,
                    usersVm = usersViewModel,
                    orderVm = ordersViewModel,
                    createOrderVm = orderCreatingViewModel,
                    loginVm = loginViewModel
                )
            }
        }
    }
}

