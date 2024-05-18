package com.example.vhsrental.ui.screens.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.vhsrental.R
import com.example.vhsrental.data.models.Format
import com.example.vhsrental.data.models.Genre
import com.example.vhsrental.ui.screens.OptionSelectDropdownMenu
import com.example.vhsrental.ui.theme.Paddings
import com.example.vhsrental.ui.viewmodels.MovieEditActions
import com.example.vhsrental.ui.viewmodels.MovieEditingUiState
import com.example.vhsrental.ui.viewmodels.MoviesUiState
import java.time.LocalDate


@Composable
fun MovieEditScreen(
    state: MovieEditingUiState,
    onConfirm: () -> Unit,
    onValueChange: (MovieEditActions) -> Unit,
    modifier: Modifier = Modifier
) {
    val textFieldModifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .padding(Paddings.small)
    val ageRatingOptions = listOf(
        stringResource(id = R.string.filter_movie_age_3),
        stringResource(id = R.string.filter_movie_age_8),
        stringResource(id = R.string.filter_movie_age_12),
        stringResource(id = R.string.filter_movie_age_15),
        stringResource(id = R.string.filter_movie_age_18)
    ).zip(listOf(3L, 8L, 12L, 15L, 18L))
    val genreOptions = Genre.entries.map { it.name }.zip(Genre.entries)
    val formatOptions = Format.entries.map { it.name }.zip(Format.entries)
    val releaseYearOptions = (1920..LocalDate.now().year).map { it.toString() }
        .zip((1920L..LocalDate.now().year)).reversed()
    val heading =
        if (state.movie == null) stringResource(id = R.string.add_movie_button)
        else stringResource(id = R.string.edit_movie_button) + state.movie.name
    val confirmButtonText =
        if (state.movie == null) stringResource(id = R.string.add_movie_confirm_button)
        else stringResource(id = R.string.confirm_changes_button)

    LazyColumn (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(Paddings.large)
    ) {
        item {
            Text(
                text = heading,
                fontSize = 35.sp
            )
        }
        item {
            OutlinedTextField(
                value = state.newName ?: "",
                onValueChange = { onValueChange(MovieEditActions.OnNameChange(it)) },
                label = { Text(text = stringResource(id = R.string.namePlaceholder)) },
                placeholder = { Text(state.movie?.name ?: "") },
                modifier = textFieldModifier
            )
        }
        item {
            OutlinedTextField(
                value = state.newLength ?: "",
                onValueChange = { onValueChange(MovieEditActions.OnLengthChange(it)) },
                label = { Text(text = stringResource(id = R.string.movie_length)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = { Text(state.movie?.length.toString()) },
                modifier = textFieldModifier
            )
        }
        item {
            OutlinedTextField(
                value = state.newAvailableCopies ?: "",
                onValueChange = { onValueChange(MovieEditActions.OnAvailableCopiesChange(it)) },
                label = { Text(text = stringResource(id = R.string.movie_available_copies)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = { Text(state.movie?.availableCopies.toString()) },
                modifier = textFieldModifier
            )
        }
        item {
            Text(text = stringResource(id = R.string.filter_movie_age_heading))
        }
        item {
            OptionSelectDropdownMenu(
                displaySelected = state.newAgeRating?.toString()
                    ?: state.movie?.ageRating.toString(),
                options = ageRatingOptions,
                onSelection = { onValueChange(MovieEditActions.OnAgeRatingChange(it)) }
            )
        }
        item {
            Text(text = stringResource(id = R.string.filter_movie_format_heading))
        }
        item {
            OptionSelectDropdownMenu(
                displaySelected = state.newFormat?.name ?: state.movie?.format?.name ?: "",
                options = formatOptions,
                onSelection = { onValueChange(MovieEditActions.OnFormatChange(it)) }
            )
        }
        item {
            OutlinedTextField(
                value = state.newImageUrl ?: "",
                onValueChange = { onValueChange(MovieEditActions.OnImageUrlChange(it)) },
                label = { Text(text = stringResource(id = R.string.movie_poster_url)) },
                placeholder = { Text(state.movie?.imageUrl ?: "") },
                modifier = textFieldModifier
            )
        }
        item {
            OutlinedTextField(
                value = state.newImdbUrl ?: "",
                onValueChange = { onValueChange(MovieEditActions.OnImdbUrlChange(it)) },
                label = { Text(text = stringResource(id = R.string.movie_imdb_url)) },
                placeholder = { Text(state.movie?.imdbUrl ?: "") },
                modifier = textFieldModifier
            )
        }
        item {
            Text(text = stringResource(id = R.string.movie_year))
        }
        item {
            OptionSelectDropdownMenu(
                displaySelected = state.newReleaseYear?.toString()
                    ?: state.movie?.releaseYear.toString(),
                options = releaseYearOptions,
                onSelection = { onValueChange(MovieEditActions.OnReleaseYearChange(it)) }
            )
        }
        item {
            OutlinedTextField(
                value = state.newDescription ?: "",
                onValueChange = { onValueChange(MovieEditActions.OnDescriptionChange(it)) },
                label = { Text(text = stringResource(id = R.string.movie_description)) },
                placeholder = { Text(state.movie?.description ?: "") },
                modifier = textFieldModifier
            )
        }
        item {
            Text(text = stringResource(id = R.string.movie_genre))
        }
        item {
            OptionSelectDropdownMenu(
                displaySelected = state.newGenre?.name ?: state.movie?.genre?.name ?: "",
                options = genreOptions,
                onSelection = { onValueChange(MovieEditActions.OnGenreChange(it)) }
            )
        }
        item {
            Button(
                onClick = { onConfirm() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = confirmButtonText, textAlign = TextAlign.Center)
            }
        }
    }
}
