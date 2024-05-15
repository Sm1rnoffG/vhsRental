package com.example.vhsrental.ui.screens.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.ui.screens.Dot
import com.example.vhsrental.ui.viewmodels.MoviesUiState

@Composable
fun MovieDetail(
    movie: DomainMovie,
    onEdit: (DomainMovie) -> Unit,
    onReservation: (DomainMovie) -> Unit,
    onDelete: (DomainMovie) -> Unit,
    asEmployee: Boolean,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier. fillMaxWidth()
    ) {
        Poster(
            url = movie.imageUrl,
            isPreview = false
        )
        Text(
            text = movie.name,
            textAlign = TextAlign.Center
        )
        Row (horizontalArrangement = Arrangement.Center) {
            Text(text = movie.format.name)
            Dot()
            Text(text = "${movie.currentlyAvailable}/${movie.availableCopies} available")
        }
        Row (horizontalArrangement = Arrangement.Center) {
            GenreChip(genre = movie.genre)
            AgeRatingChip(rating = movie.ageRating)
            LengthChip(length = movie.length)
        }
        MovieActionButtons(
            onEdit = { onEdit(movie) },
            onReserve = { onReservation(movie) },
            onDelete = { onDelete(movie) },
            asEmployee = asEmployee
        )
        Text(
            text = movie.description,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = { /* TODO open URL */ },
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "IMDB page", textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun MovieActionButtons(
    onEdit: () -> Unit,
    onReserve: () -> Unit,
    onDelete: () -> Unit,
    asEmployee: Boolean,
    modifier: Modifier = Modifier
) {
    val buttonWidth = Modifier
        .width(15.dp)
        .wrapContentHeight()
    val buttons = mutableListOf(Pair("Reserve", onReserve))
    if (asEmployee) buttons.addAll(listOf(
        Pair("Edit", onEdit),
        Pair("Delete", onDelete)
    ))

    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth(),
    ) {
        buttons.forEach {
            Button(onClick = { it.second }) {
                Text(
                    text = it.first,
                    textAlign = TextAlign.Center,
                    modifier = buttonWidth
                )
            }
        }
    }
}


