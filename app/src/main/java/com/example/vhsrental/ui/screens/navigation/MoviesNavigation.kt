package com.example.vhsrental.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.screens.movies.CatalogueScreen
import com.example.vhsrental.ui.screens.movies.MovieDetail
import com.example.vhsrental.ui.screens.movies.MovieEditScreen
import com.example.vhsrental.ui.viewmodels.LoginActions
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MovieUiState
import com.example.vhsrental.ui.viewmodels.MoviesViewModel

@Composable
fun MoviesNavigation(navController: NavHostController, movieVm: MoviesViewModel, userRole: Role) {
    NavHost(navController = navController, startDestination = "catalogue") {
        composable("movies") {
            movieVm.emitAction(MovieActions.ToCatalogue)
            CatalogueScreen(movieList = (movieVm.uiStateFlow.collectAsState().value as MovieUiState.Catalogue).movies,
                toMovieDetail = { movie -> navigateToMovieDetail(movie, movieVm, navController) },
                onQuerryRequest = { /*TODO*/ })
        }
        composable("movies/movie_detail") {
            MovieDetail(
                movie = (movieVm.uiStateFlow.collectAsState().value as MovieUiState.MovieDetail).movie,
                onMovieAction = {},
                asEmployee = userRole == Role.Employee
            )
        }
        composable("movies/movie_detail/edit_movie") {
            MovieEditScreen(state = (movieVm.uiStateFlow.collectAsState().value as MovieUiState.MovieEditing),
                onConfirm = { movieVm.emitAction(MovieActions.SaveChanges); navController.popBackStack() },
                onValueChange = { action -> movieVm.emitEditAction(action) })
        }
        composable("movies/add_movie") {
            MovieEditScreen(state = (movieVm.uiStateFlow.collectAsState().value as MovieUiState.MovieEditing),
                onConfirm = { movieVm.emitAction(MovieActions.AddMovie); navController.popBackStack() },
                onValueChange = { action -> movieVm.emitEditAction(action) })
        }
    }
}

fun navigateToMovieDetail(movie: DomainMovie, vm: MoviesViewModel, navController: NavController) {
    vm.emitAction(MovieActions.OpenMovieDetail(movie))
    navController.navigate("movies/movie_detail")
}