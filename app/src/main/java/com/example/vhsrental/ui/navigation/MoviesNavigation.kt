package com.example.vhsrental.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.screens.movies.CatalogueScreen
import com.example.vhsrental.ui.screens.movies.MovieDetail
import com.example.vhsrental.ui.screens.movies.MovieEditScreen
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MovieUiState
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderActions
import com.example.vhsrental.ui.viewmodels.OrderViewModel

@Composable
fun MoviesNavigation(
    navController: NavHostController,
    movieVm: MoviesViewModel,
    orderVm: OrderViewModel,
    user: DomainUser
) {
    NavHost(navController = navController, startDestination = "catalogue") {
        composable("catalogue") {
            CatalogueScreen(state = (movieVm.uiStateFlow.collectAsState().value),
                toMovieDetail = { movie -> navigateToMovieDetail(movie, movieVm, navController) },
                onAddMovie = { navController.navigate("add_movie") },
                onQuerryRequest = { /*TODO*/ })
        }
        composable("movie_detail") {
            val movie = (movieVm.uiStateFlow.collectAsState().value as MovieUiState.MovieDetail).movie
            MovieDetail(
                movie = movie,
                onMovieAction = { orderVm.emitOrderAction(OrderActions.OnReservationRequest(movie, user)) },
                asEmployee = user.role == Role.Employee
            )
        }
        composable("edit_movie") {
            MovieEditScreen(state = (movieVm.uiStateFlow.collectAsState().value as MovieUiState.MovieEditing),
                onConfirm = {
                    movieVm.emitAction(MovieActions.SaveChanges)
                    if ((movieVm.uiStateFlow.value as MovieUiState.MovieEditing).errorMessage.isEmpty()) {
                        movieVm.emitAction(MovieActions.ToCatalogue)
                        navController.popBackStack()
                    }
                },
                onValueChange = { action -> movieVm.emitEditAction(action) })
        }
        composable("add_movie") {
            MovieEditScreen(
                state = (movieVm.uiStateFlow.collectAsState().value as MovieUiState.MovieEditing),
                onConfirm = {
                    movieVm.emitAction(MovieActions.ConfirmAddMovie)
                    if ((movieVm.uiStateFlow.value as MovieUiState.MovieEditing).errorMessage.isEmpty()) {
                        movieVm.emitAction(MovieActions.ToCatalogue)
                        navController.popBackStack()
                    }
                },
                onValueChange = { action -> movieVm.emitEditAction(action) }
            )
        }
    }
}

fun navigateToMovieDetail(movie: DomainMovie, vm: MoviesViewModel, navController: NavController) {
    vm.emitAction(MovieActions.OpenMovieDetail(movie))
    navController.navigate("movie_detail")
}