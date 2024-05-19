package com.example.vhsrental.ui.screens.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vhsrental.R
import com.example.vhsrental.data.models.DomainMovie
import com.example.vhsrental.data.models.Format
import com.example.vhsrental.data.models.Genre
import com.example.vhsrental.ui.screens.EmptyQuery
import com.example.vhsrental.ui.theme.Paddings

@Composable
fun CatalogueScreen (
    movies: List<DomainMovie>,
    toMovieDetail: (DomainMovie) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    if (movies.isEmpty()) {
        EmptyQuery(message = stringResource(id = R.string.no_movies))
    } else {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxWidth()
                .padding(Paddings.medium)
        ) {
            itemsIndexed(movies) { _, movie ->
                Surface(onClick = { toMovieDetail(movie) }) {
                    MovieCard(
                        movie = movie,
                        modifier = Modifier
                            .padding(Paddings.small / 2)
                    )
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
    Surface (
        shape = RoundedCornerShape(5.dp),
        color = Color.LightGray,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.large)
        ) {
            Poster(movie.imageUrl, true)
            MoviePreview(movie = movie)
        }
    }
}

@Composable
fun Poster(url: String, isPreview: Boolean, modifier: Modifier = Modifier) {
    val size = if (isPreview) 100.dp else 300.dp

    Surface (
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        modifier = modifier
            .size(width = size, height = size.times(1.5F))
    ) {
        AsyncImage(
            model = url,
            contentDescription = stringResource(id = R.string.poster_desc) + url,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
        )
    }
}

@Composable
fun MoviePreview(movie: DomainMovie, modifier: Modifier = Modifier) {
    Column (
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = movie.name,
            textAlign = TextAlign.Right,
            fontSize = 35.sp
        )
        Row (
            modifier = Modifier
                .padding(Paddings.small)
        ) {
            FormatChip(format = movie.format)
            Spacer(modifier = Modifier.size(10.dp))
            AvailableChip(isAvailable = movie.currentlyAvailable > 0)
        }
        Row {
            GenreChip(genre = movie.genre)
            Spacer(modifier = Modifier.size(10.dp))
            AgeRatingChip(rating = movie.ageRating)
            Spacer(modifier = Modifier.size(10.dp))
            LengthChip(length = movie.length)
        }
    }
}

@Composable
fun AvailableChip (
    isAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    Surface (
        color = Color.DarkGray,
        shape = RoundedCornerShape(size = 5.dp),
        modifier = modifier
            .wrapContentSize()
    ) {
        Text(
            text =
                if (isAvailable) stringResource(id = R.string.available)
                else stringResource(id = R.string.not_available),
            color = if (isAvailable) Color.Green else Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(Paddings.medium)
        )
    }
}

@Composable
fun FormatChip (
    format: Format,
    modifier: Modifier = Modifier
) {
    Surface (
        color = Color.DarkGray,
        shape = RoundedCornerShape(size = 5.dp),
        modifier = modifier
            .wrapContentSize()
    ) {
        Text(
            text = format.name,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(Paddings.medium)
        )
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
            .wrapContentSize()
    ) {
        Text(
            text = genre.name,
            color = if (chipColor == Color.Yellow) Color.Black else Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(Paddings.medium)
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
            .wrapContentSize()
    ) {
        Text(
            text = "$rating+",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(Paddings.medium)
        )
    }
}

@Composable
fun LengthChip(length: Long, modifier: Modifier = Modifier) {
    Surface (
        color = Color.Gray,
        shape = RoundedCornerShape(size = 5.dp),
        modifier = modifier
            .wrapContentSize()
    ) {
        Text(
            text = "$length min",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(Paddings.medium)
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
        imageUrl = "https://image.tmdb.org/t/p/original/nlV35BhheFq9QqIdiL8aMCa3zAm.jpg",
        imdbUrl = "url",
        releaseYear = 12,
        description = "a",
        genre = Genre.Horror
    )

    MovieCard(movie = movie)
}
