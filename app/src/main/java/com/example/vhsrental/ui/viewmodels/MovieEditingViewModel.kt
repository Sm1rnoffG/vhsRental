package com.example.vhsrental.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vhsrental.data.exceptions.MovieExceptions
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.Format
import com.example.vhsrental.data.models.Genre
import com.example.vhsrental.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed class MovieEditActions {
    data class OnMovieRent(val movie: DomainMovie?) : MovieEditActions()
    data class OnMovieReturn(val movie: DomainMovie?) : MovieEditActions()
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
    data class OnLoadMovie(val movie: DomainMovie) : MovieEditActions()
    data object OnSaveChanges : MovieEditActions()
    data object OnAddMovie : MovieEditActions()
    data object OnBeginAddMovie : MovieEditActions()
}

data class MovieEditingUiState (
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
)

fun MovieEditingUiState.updateMovie() : DomainMovie {
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

    return movie
}

fun MovieEditingUiState.createMovie() : DomainMovie {
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
class MovieEditingViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(MovieEditingUiState())
    val uiStateFlow: StateFlow<MovieEditingUiState>
        get() = _uiStateFlow

    fun emitEditAction(action: MovieEditActions) {
        when (action) {
            is MovieEditActions.OnAgeRatingChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newAgeRating = action.update) }
            is MovieEditActions.OnAvailableCopiesChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newAvailableCopies = action.update) }
            is MovieEditActions.OnDescriptionChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newDescription = action.update) }
            is MovieEditActions.OnFormatChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newFormat = action.update) }
            is MovieEditActions.OnGenreChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newGenre = action.update) }
            is MovieEditActions.OnImageUrlChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newImageUrl = action.update) }
            is MovieEditActions.OnImdbUrlChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newImdbUrl = action.update) }
            is MovieEditActions.OnLengthChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newLength = action.update) }
            is MovieEditActions.OnNameChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newName = action.update) }
            is MovieEditActions.OnReleaseYearChange ->
                _uiStateFlow.update { uiStateFlow.value.copy(newReleaseYear = action.update) }
            is MovieEditActions.OnLoadMovie ->
                _uiStateFlow.update { MovieEditingUiState(movie = action.movie) }
            is MovieEditActions.OnBeginAddMovie ->
                _uiStateFlow.update { MovieEditingUiState() }
            is MovieEditActions.OnAddMovie -> addMovie()
            is MovieEditActions.OnSaveChanges -> saveChanges()
            is MovieEditActions.OnMovieRent -> rentMovie(action.movie)
            is MovieEditActions.OnMovieReturn -> returnMovie(action.movie)
        }
    }

    private fun saveChanges() {
        repository.updateMovie(uiStateFlow.value.updateMovie())
        _uiStateFlow.update { MovieEditingUiState() }
    }
    private fun addMovie() {
        try {
            repository.insert(uiStateFlow.value.createMovie())
            _uiStateFlow.update { MovieEditingUiState() }
        } catch (e: Exception) {
            _uiStateFlow.update { uiStateFlow.value.copy(errorMessage = e.message ?: "Error") }
        }
    }

    private fun rentMovie(movie: DomainMovie?) =
        repository.updateMovie(movie?.copy(currentlyAvailable = movie.currentlyAvailable - 1) ?:
            throw MovieExceptions.UnexpectedException())

    private fun returnMovie(movie: DomainMovie?) =
        repository.updateMovie(movie?.copy(currentlyAvailable = movie.currentlyAvailable + 1) ?:
            throw MovieExceptions.UnexpectedException())
}