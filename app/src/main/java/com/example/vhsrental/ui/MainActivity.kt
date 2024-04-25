package com.example.vhsrental.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.vhsrental.ui.AppNavigation
import com.example.vhsrental.ui.theme.VhsRentalTheme
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VhsRentalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        movieVM = moviesViewModel,
                        userVM = usersViewModel,
                        orderVM = ordersViewModel,
                        loginVM = loginViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(vm: LoginViewModel, modifier: Modifier = Modifier) {
    val name = (vm.uiStateFlow.collectAsState().value as LoginUiState.LoggedIn).user.name

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
