package com.example.vhsrental.ui.viewmodels

import android.text.BoringLayout
import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.Query
import com.example.vhsrental.data.exceptions.MovieExceptions
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainOrder
import com.example.vhsrental.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


sealed class MovieActions {
    data class OpenMovieDetail(
        val movie: DomainMovie,
        val canMovieEdit: Boolean,
        val canMovieReserve: Boolean
    ) : MovieActions()
    data class DeleteMovie(val movie: DomainMovie) : MovieActions()
    data class UpdateSearchValue(val update: String) : MovieActions()
    data class OnSearch(val searchFunction: (DomainMovie) -> String) : MovieActions()
    data class OnFilter(val filterFunction: (DomainMovie) -> Boolean) : MovieActions()
    data class OnSort(val sortFunction: Comparator<DomainMovie>, val flipped: Boolean) : MovieActions()
    data object LoadCatalogue : MovieActions()
}

data class MoviesUiState (
    val catalogue: Query<DomainMovie>,
    val displayMovie: DomainMovie? = null,
    val canMovieEdit: Boolean = false,
    val canMovieReserve: Boolean = false,
)

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiStateFlow: MutableStateFlow<MoviesUiState> =
        MutableStateFlow(MoviesUiState(Query(repository.selectAll())))
    val uiStateFlow: StateFlow<MoviesUiState>
        get() = _uiStateFlow

    fun emitAction(action: MovieActions) {
        when (action) {
            is MovieActions.DeleteMovie -> deleteMovie()
            is MovieActions.LoadCatalogue ->
                _uiStateFlow.update { uiStateFlow.value.copy(catalogue = Query(getAllMovies())) }
            is MovieActions.OpenMovieDetail ->
                _uiStateFlow.update { uiStateFlow.value.copy(
                    displayMovie = action.movie,
                    canMovieReserve = action.canMovieReserve,
                    canMovieEdit = action.canMovieEdit,
                ) }
            is MovieActions.OnFilter ->
                _uiStateFlow.update { uiStateFlow.value.copy(catalogue = uiStateFlow.value.catalogue.filter(action.filterFunction)) }
            is MovieActions.OnSearch ->
                _uiStateFlow.update { uiStateFlow.value.copy(catalogue = uiStateFlow.value.catalogue.search(action.searchFunction)) }
            is MovieActions.OnSort ->
                _uiStateFlow.update { uiStateFlow.value.copy(catalogue = uiStateFlow.value.catalogue.sort(action.sortFunction, action.flipped)) }
            is MovieActions.UpdateSearchValue ->
                _uiStateFlow.update { uiStateFlow.value.copy(catalogue = uiStateFlow.value.catalogue.updateSearch(action.update)) }
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