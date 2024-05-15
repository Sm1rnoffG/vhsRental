package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.exceptions.MovieExceptions
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


sealed class QueryRequest {
    data class Search(val column: String, val search: String) : QueryRequest()
    data class Filter(val column: String, val filter: String) : QueryRequest()
    data class Sort(val column: String) : QueryRequest()
    data object FlipOrder : QueryRequest()
}

sealed class MovieActions {
    data class OpenMovieDetail(val movie: DomainMovie) : MovieActions()
    data class DeleteMovie(val movie: DomainMovie) : MovieActions()
    data object LoadCatalogue : MovieActions()
}

data class MoviesUiState (
    val catalogue: List<DomainMovie>,
    val displayMovie: DomainMovie? = null
)

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiStateFlow: MutableStateFlow<MoviesUiState> =
        MutableStateFlow(MoviesUiState(repository.selectAll()))
    val uiStateFlow: StateFlow<MoviesUiState>
        get() = _uiStateFlow

    fun emitAction(action: MovieActions) {
        when (action) {
            is MovieActions.DeleteMovie -> deleteMovie()
            is MovieActions.LoadCatalogue ->
                _uiStateFlow.update { uiStateFlow.value.copy(catalogue = getAllMovies()) }
            is MovieActions.OpenMovieDetail ->
                _uiStateFlow.update { uiStateFlow.value.copy(displayMovie = action.movie) }
        }
    }

    private fun deleteMovie() = repository.delete(
            uiStateFlow.value.displayMovie?.id ?: throw MovieExceptions.UnexpectedException()
    )

    fun getAllMovies() : List<DomainMovie> = repository.selectAll()

    fun assignMoviesToOrders(orders: List<DomainOrder>) : List<Pair<DomainOrder, DomainMovie>> {
        val movies = repository.selectAll()
        val result = orders.map { order ->
            Pair(order, movies.find { movie ->
                movie.id == order.movie
            } ?: throw MovieExceptions.NoMovieFoundException() )
        }
        return result
    }
}