package com.example.vhsrental.ui.screens.movies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.Format
import com.example.vhsrental.data.models.Genre
import com.example.vhsrental.ui.screens.Dot
import com.example.vhsrental.ui.viewmodels.MoviesUiState

@Composable
fun CatalogueScreen (
    state: MoviesUiState,
    toMovieDetail: (DomainMovie) -> Unit,
    onQuerryRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Column (
        modifier = modifier
    ) {
        // QuerryBar()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(state.catalogue) { _, movie ->
                Surface(onClick = { toMovieDetail(movie) }) {
                    MovieCard(movie = movie)
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: DomainMovie,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Poster(movie.imageUrl, true)
        MoviePreview(movie = movie)
    }
}

@Composable
fun Poster(url: String, isPreview: Boolean, modifier: Modifier = Modifier) {
    val size = if (isPreview) 30.dp else 60.dp

    Surface (
        shape = RoundedCornerShape(corner = CornerSize(2.dp)),
        modifier = modifier
            .size(size)
    ) {
        AsyncImage(
            model = url,
            contentDescription = "Movie poster from: $url",
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun MoviePreview(movie: DomainMovie, modifier: Modifier = Modifier) {
    Column (
        horizontalAlignment = Alignment.End,
        modifier = modifier
    ) {
        Text(
            text = movie.name,
            textAlign = TextAlign.Right
        )
        Row  {
            Text(text = movie.format.name)
            Dot()
            if (movie.availableCopies > 0) {
                Text(text = "Available", color = Color.Green)
            } else {
                Text(text = "Not available", color = Color.Red)
            }
        }
        Row {
            GenreChip(genre = movie.genre)
            AgeRatingChip(rating = movie.ageRating)
            LengthChip(length = movie.length)
        }
    }
}

@Composable
fun GenreChip(genre: Genre, modifier: Modifier = Modifier) {
    val chipColor = when (genre) {
        Genre.Action -> Color.Blue
        Genre.Comedy -> Color.Yellow
        Genre.Family -> Color.Green
        Genre.Horror -> Color.Black
        Genre.Romantic -> Color.Red
    }

    Surface (
        color = chipColor,
        shape = RoundedCornerShape(size = 5.dp),
        modifier = modifier
    ) {
        Text(
            text = genre.name,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AgeRatingChip(rating: Long, modifier: Modifier = Modifier) {
    val chipColor = when (rating) {
        3L -> Color.Green
        8L -> Color.Blue
        12L -> Color.Yellow
        15L -> Color.Red
        else -> Color.Black
    }

    Surface (
        color = chipColor,
        shape = RoundedCornerShape(size = 5.dp),
        modifier = modifier
    ) {
        Text(
            text = "$rating+",
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LengthChip(length: Long, modifier: Modifier = Modifier) {
    Surface (
        color = Color.Gray,
        shape = RoundedCornerShape(size = 5.dp),
        modifier = modifier
    ) {
        Text(
            text = "$length min",
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun Movie() {
    val movie = DomainMovie(
        id = 1,
        name = "film",
        length = 50,
        availableCopies = 5,
        currentlyAvailable = 4,
        format = Format.DVD,
        ageRating = 12,
        imageUrl = "",
        imdbUrl = "url",
        releaseYear = 12,
        description = "a",
        genre = Genre.Horror
    )

    MovieCard(movie = movie)
}
