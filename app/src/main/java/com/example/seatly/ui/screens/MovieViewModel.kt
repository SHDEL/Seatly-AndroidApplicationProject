package com.example.seatly.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.seatly.MovieShowNowApplication
import com.example.seatly.data.MovieRepository
import com.example.seatly.model.Genre
import com.example.seatly.model.Movie
import kotlinx.coroutines.launch
import java.io.IOException


sealed interface MovieUiState {
    data class Success(val movies: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

@RequiresApi(Build.VERSION_CODES.O)
class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    private var nowShowingMovies: List<Movie> = emptyList()
    private var comingSoonMovies: List<Movie> = emptyList()
    private var allMovies: List<Movie> = emptyList()

    init {
        getNowShowingWithGenres()
    }

    fun getMovieById(movieId: Int?): Movie? {
        return allMovies.find { it.id == movieId }
    }

    fun getNowShowingWithGenres() {
        viewModelScope.launch {
            movieUiState = MovieUiState.Loading
            try {
                val genres = movieRepository.getGenres()
                val movies = movieRepository.getMovies()
                nowShowingMovies = addGenreToMovie(movies, genres)
                allMovies = nowShowingMovies + comingSoonMovies
                movieUiState = MovieUiState.Success(nowShowingMovies)
            } catch (e: IOException) {
                movieUiState = MovieUiState.Error
            }
        }
    }

    fun getComingWithGenres() {
        viewModelScope.launch {
            movieUiState = MovieUiState.Loading
            try {
                val genres = movieRepository.getGenres()
                val movies = movieRepository.getComingSoon()
                comingSoonMovies = addGenreToMovie(movies, genres)
                allMovies = nowShowingMovies + comingSoonMovies
                movieUiState = MovieUiState.Success(comingSoonMovies)
            } catch (e: IOException) {
                movieUiState = MovieUiState.Error
            }
        }
    }


    private fun addGenreToMovie(movies: List<Movie>, genres: List<Genre>): List<Movie> {
        return movies.map { movie ->
            val genreNames = movie.genre.mapNotNull { genreId ->
                genres.find { it.id == genreId }?.name
            }
            movie.copy(genreName = genreNames)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MovieShowNowApplication)
                val movieRepository = application.container.movieRepository
                MovieViewModel(movieRepository = movieRepository)
            }
        }
    }
}