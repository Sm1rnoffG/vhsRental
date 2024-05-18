package com.example.vhsrental.ui.screens.movies

import android.graphics.Movie
import android.text.BoringLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.ui.screens.Dot
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
                Text(text = "${movie.currentlyAvailable}/${movie.availableCopies} available")
            }
            Row(horizontalArrangement = Arrangement.Center) {
                GenreChip(genre = movie.genre, modifier = Modifier.padding(Paddings.small))
                AgeRatingChip(rating = movie.ageRating, modifier = Modifier.padding(Paddings.small))
                LengthChip(length = movie.length, modifier = Modifier.padding(Paddings.small))
            }
            MovieActionButtons(
                onEdit = { onEdit(movie) },
                onReserve = { onReservation(movie) },
                onDelete = { onDelete(movie) },
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
                Text(text = "IMDB page", textAlign = TextAlign.Center)
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
            enabledText = "Reserve movie",
            disabledText = if (isAvailable) "Already rented" else "Not available",
            enabled = canReserve && isAvailable,
            onClick = onReserve,
            modifier = Modifier.padding(Paddings.small)
        )
        if (asEmployee) {
            MovieActionButton(
                enabledText = "Edit movie",
                disabledText = "Movie in use",
                enabled = canEdit,
                onClick = onEdit,
                modifier = Modifier.padding(Paddings.small)
            )
            MovieActionButton(
                enabledText = "Delete movie",
                disabledText = "Movie in use",
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
