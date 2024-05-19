package com.example.vhsrental.ui.screens.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.vhsrental.R
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.ui.screens.Dot
import com.example.vhsrental.ui.screens.users.FinalWarning
import com.example.vhsrental.ui.theme.Paddings

@Composable
fun MovieDetail(
    movie: DomainMovie,
    canEdit: Boolean,
    canReserve: Boolean,
    onEdit: (DomainMovie) -> Unit,
    onReservation: (DomainMovie) -> Unit,
    onDelete: (DomainMovie) -> Unit,
    asEmployee: Boolean,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    var displayWarning by remember { mutableStateOf(false) }
    var displayReserve by remember { mutableStateOf(false) }

    when {
        displayWarning -> FinalWarning(
            heading = stringResource(id = R.string.permanent_deletion),
            text = stringResource(id = R.string.delete_movie_dialog),
            onConfirm = {
                onDelete(movie)
                displayWarning = false
            },
            onCancel = { displayWarning = false }
        )
        displayReserve -> FinalWarning(
            heading = stringResource(id = R.string.reservation_heading),
            text = stringResource(id = R.string.reservation_dialog),
            onConfirm = {
                onReservation(movie)
                displayReserve = false
            },
            onCancel = { displayReserve = false }
        )
    }

    LazyColumn (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier. fillMaxWidth()
    ) {
        items (count = 1) {
            Poster(
                url = movie.imageUrl,
                isPreview = false
            )
            Text(
                text = movie.name,
                textAlign = TextAlign.Center,
                fontSize = 35.sp
            )
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = movie.format.name)
                Dot()
                Text(
                    text = "${movie.currentlyAvailable}/${movie.availableCopies} ${
                        stringResource(id = R.string.available)}"
                )
            }
            Row(horizontalArrangement = Arrangement.Center) {
                GenreChip(genre = movie.genre, modifier = Modifier.padding(Paddings.small))
                AgeRatingChip(rating = movie.ageRating, modifier = Modifier.padding(Paddings.small))
                LengthChip(length = movie.length, modifier = Modifier.padding(Paddings.small))
            }
            MovieActionButtons(
                onEdit = { onEdit(movie) },
                onReserve = { displayReserve = true },
                onDelete = { displayWarning = true },
                isAvailable = movie.currentlyAvailable > 0,
                asEmployee = asEmployee,
                canReserve = canReserve,
                canEdit = canEdit
            )
            Text(
                text = movie.description,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedButton(
                onClick = { uriHandler.openUri(movie.imdbUrl) },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = stringResource(id = R.string.imdb_button), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun MovieActionButtons(
    onEdit: () -> Unit,
    onReserve: () -> Unit,
    onDelete: () -> Unit,
    asEmployee: Boolean,
    isAvailable: Boolean,
    canEdit: Boolean,
    canReserve: Boolean,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        MovieActionButton(
            enabledText = stringResource(id = R.string.res_movie_button),
            disabledText =
                if (isAvailable) stringResource(id = R.string.already_rented)
                else stringResource(id = R.string.not_available),
            enabled = canReserve && isAvailable,
            onClick = onReserve,
            modifier = Modifier.padding(Paddings.small)
        )
        if (asEmployee) {
            MovieActionButton(
                enabledText = stringResource(id = R.string.edit_movie_button),
                disabledText = stringResource(id = R.string.movie_used),
                enabled = canEdit,
                onClick = onEdit,
                modifier = Modifier.padding(Paddings.small)
            )
            MovieActionButton(
                enabledText = stringResource(id = R.string.delete_movie_button),
                disabledText = stringResource(id = R.string.movie_used),
                enabled = canEdit,
                onClick = onDelete,
                modifier = Modifier.padding(Paddings.small)
            )
        }
    }
}

@Composable
fun MovieActionButton(
    enabledText: String,
    disabledText: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .wrapContentSize()
    ) {
        if (enabled) {
            Text(
                text = enabledText,
                textAlign = TextAlign.Center,
            )
        } else {
            Text(
                text = disabledText,
                textAlign = TextAlign.Center,
            )
        }
    }
}
