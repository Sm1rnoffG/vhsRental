package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vhsrental.data.exceptions.MovieExceptions
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.DomainUser
import com.example.vhsrental.data.models.Format
import com.example.vhsrental.data.models.Genre
import com.example.vhsrental.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class QueryRequest {
    data class Search(val column: String, val search: String) : QueryRequest()
    data class Filter(val column: String, val filter: String) : QueryRequest()
    data class Sort(val column: String) : QueryRequest()
    data object FlipOrder : QueryRequest()
}

sealed class MovieEditActions {
    data class OnNameChange(val update: String) : MovieEditActions()
    data class OnLengthChange(val update: String) : MovieEditActions()
    data class OnAvailableCopiesChange(val update: String) : MovieEditActions()
    data class OnFormatChange(val update: Format) : MovieEditActions()
    data class OnAgeRatingChange(val update: Long) : MovieEditActions()
    data class OnImageUrlChange(val update: String) : MovieEditActions()
    data class OnImdbUrlChange(val update: String) : MovieEditActions()
    data class OnReleaseYearChange(val update: Long) : MovieEditActions()
    data class OnDescriptionChange(val update: String) : MovieEditActions()
    data class OnGenreChange(val update: Genre) : MovieEditActions()
}

sealed class MovieActions {
    data class Reserve(val user: DomainUser) : MovieActions()
    data class EditMovie(val movie: DomainMovie) : MovieActions()
    data class OpenMovieDetail(val movie: DomainMovie) : MovieActions()
    data object SaveChanges : MovieActions()
    data object AddMovie : MovieActions()
    data object ConfirmAddMovie : MovieActions()
    data object ToCatalogue : MovieActions()
}

sealed class MovieUiState {
    data class Catalogue(
        val movies: List<DomainMovie> = emptyList()
    ) : MovieUiState()
    data class MovieEditing(
        val movie: DomainMovie? = null,
        val newName: String? = null,
        val newLength: String? = null,
        val newAvailableCopies: String? = null,
        val newFormat: Format? = null,
        val newAgeRating: Long? = null,
        val newImageUrl: String? = null,
        val newImdbUrl: String? = null,
        val newReleaseYear: Long? = null,
        val newDescription: String? = null,
        val newGenre: Genre? = null,
        val errorMessage: String = ""
    ) : MovieUiState()
    data class MovieDetail(val movie: DomainMovie) : MovieUiState()
}

fun MovieUiState.MovieEditing.updateMovie() {
    if (movie == null) throw MovieExceptions.NotEditingException()

    movie.name = newName ?: movie.name
    movie.length = newLength?.toLong() ?: movie.length
    movie.availableCopies = newAvailableCopies?.toLong() ?: movie.availableCopies
    movie.ageRating = newAgeRating ?: movie.ageRating
    movie.imageUrl = newImageUrl ?: movie.imageUrl
    movie.imdbUrl = newImdbUrl ?: movie.imdbUrl
    movie.releaseYear = newReleaseYear ?: movie.releaseYear
    movie.description = newDescription ?: movie.description
    movie.genre = newGenre ?: movie.genre
    movie.format = newFormat ?: movie.format
}

fun MovieUiState.MovieEditing.createMovie() : DomainMovie {
    if (movie != null) throw MovieExceptions.NotCreatingNewException()

    return DomainMovie(
        id = -1,
        name = newName ?: throw MovieExceptions.NotEnoughDataExceptions(),
        ageRating = newAgeRating ?: throw MovieExceptions.NotEnoughDataExceptions(),
        length = newLength?.toLong() ?: throw MovieExceptions.NotEnoughDataExceptions(),
        format = newFormat ?: throw MovieExceptions.NotEnoughDataExceptions(),
        imdbUrl = newImdbUrl ?: throw MovieExceptions.NotEnoughDataExceptions(),
        imageUrl = newImageUrl ?: throw MovieExceptions.NotEnoughDataExceptions(),
        genre = newGenre ?: throw MovieExceptions.NotEnoughDataExceptions(),
        availableCopies = newAvailableCopies?.toLong() ?: throw MovieExceptions.NotEnoughDataExceptions(),
        currentlyAvailable = newAvailableCopies.toLong(),
        description = newDescription ?: throw MovieExceptions.NotEnoughDataExceptions(),
        releaseYear = newReleaseYear ?: throw MovieExceptions.NotEnoughDataExceptions(),
    )
}

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiStateFlow: MutableStateFlow<MovieUiState> =
        MutableStateFlow(MovieUiState.Catalogue(emptyList()))
    val uiStateFlow: StateFlow<MovieUiState>
        get() = _uiStateFlow

    init {
        viewModelScope.launch {
            _uiStateFlow.update { MovieUiState.Catalogue(repository.selectAll()) }
        }
    }

    fun emitAction(action: MovieActions) {
        when (action) {
            is MovieActions.EditMovie -> _uiStateFlow.update { MovieUiState.MovieEditing(action.movie) }
            is MovieActions.Reserve -> reserveMovie(action.user.id)
            is MovieActions.SaveChanges -> saveChanges()
            is MovieActions.OpenMovieDetail -> _uiStateFlow.update { MovieUiState.MovieDetail(action.movie) }
            is MovieActions.AddMovie -> _uiStateFlow.update { MovieUiState.MovieEditing() }
            is MovieActions.ConfirmAddMovie -> addMovie()
            is MovieActions.ToCatalogue ->
                _uiStateFlow.update { MovieUiState.Catalogue(getAllMovies()) }
        }
    }

    fun emitEditAction(action: MovieEditActions) {
        val currentState = _uiStateFlow.value as MovieUiState.MovieEditing

        when (action) {
            is MovieEditActions.OnAgeRatingChange ->
                _uiStateFlow.update { currentState.copy() }
            is MovieEditActions.OnAvailableCopiesChange ->
                _uiStateFlow.update { currentState.copy(newAvailableCopies = action.update) }
            is MovieEditActions.OnDescriptionChange ->
                _uiStateFlow.update { currentState.copy(newDescription = action.update) }
            is MovieEditActions.OnFormatChange ->
                _uiStateFlow.update { currentState.copy(newFormat = action.update) }
            is MovieEditActions.OnGenreChange ->
                _uiStateFlow.update { currentState.copy(newGenre = action.update) }
            is MovieEditActions.OnImageUrlChange ->
                _uiStateFlow.update { currentState.copy(newImageUrl = action.update) }
            is MovieEditActions.OnImdbUrlChange ->
                _uiStateFlow.update { currentState.copy(newImdbUrl = action.update) }
            is MovieEditActions.OnLengthChange ->
                _uiStateFlow.update { currentState.copy(newLength = action.update) }
            is MovieEditActions.OnNameChange ->
                _uiStateFlow.update { currentState.copy(newName = action.update) }
            is MovieEditActions.OnReleaseYearChange ->
                _uiStateFlow.update { currentState.copy(newReleaseYear = action.update) }
        }
    }

    fun getAllMovies() : List<DomainMovie> {
        var movies = emptyList<DomainMovie>();
        viewModelScope.launch (Dispatchers.IO) {
            movies = repository.selectAll()
        }
        return movies
    }

    private fun addMovie() {
        val currentState = _uiStateFlow.value as MovieUiState.MovieEditing
        try {
            val movie = currentState.createMovie()

            viewModelScope.launch {
                repository.insert(movie)
            }
        } catch (e: Exception) {
            _uiStateFlow.update { currentState.copy(errorMessage = e.message ?: "Error") }
        }
    }

    private fun saveChanges() {
        val currentState = _uiStateFlow.value as MovieUiState.MovieEditing
        currentState.updateMovie()

        viewModelScope.launch {

        }
    }

    private fun reserveMovie(userID: Long) {
        val currentState = _uiStateFlow.value as MovieUiState.MovieDetail

        viewModelScope.launch {
            // TODO send new reservation
        }
    }
}