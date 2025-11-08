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
import com.example.seatly.data.NetworkMovieRepository
import com.example.seatly.model.GenreResponse
import com.example.seatly.model.Movie
import com.example.seatly.network.MovieApiService
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


sealed interface MovieUiState {
    data class Success(val movies: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}




@RequiresApi(Build.VERSION_CODES.O)
class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel(){
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    var selectedTab by mutableStateOf("")
        private set

    init {
        getNowShowingWithGenres()

    }

    fun onTabSelect(tab: String) {
        selectedTab = tab
        if (selectedTab == "Now Showing") {
            getNowShowingWithGenres()
        }
        else {
            getComingWithGenres()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNowShowingWithGenres(){
        viewModelScope.launch {
            movieUiState = MovieUiState.Loading
            try {
                val moviesResponse = movieRepository.getMovies()
                val genresResponse = movieRepository.getGenres()
                val genreMap = genresResponse.associate { it.id to it.name }

                val moviesGenres = moviesResponse.map { movie ->
                    val genreName = movie.genre.mapNotNull { genreMap[it]}
                    val formattedDate = movie.releaseDate?.let { dateString ->
                        try {
                            val localDate = LocalDate.parse(dateString)
                            localDate.format(
                                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    movie.copy(genreName = genreName, releaseDate = formattedDate)
                }
                movieUiState = MovieUiState.Success(moviesGenres)

            } catch (e: IOException) {
                movieUiState = MovieUiState.Error
            } catch (e: retrofit2.HttpException) {
                movieUiState = MovieUiState.Error
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getComingWithGenres(){
        viewModelScope.launch {
            movieUiState = MovieUiState.Loading
            try {
                val movieResponse = movieRepository.getComingSoon()
                val genresResponse = movieRepository.getGenres()
                val genreMap = genresResponse.associate { it.id to it.name }

                val moviesGenres = movieResponse.map { movie ->
                    val genreName = movie.genre.mapNotNull { genreMap[it] }
                        val formattedDate = movie.releaseDate?.let { dateString ->
                            try {
                                val localDate = LocalDate.parse(dateString)
                                localDate.format(
                                    DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }
                        movie.copy(genreName = genreName, releaseDate = formattedDate)
                }
                movieUiState = MovieUiState.Success(moviesGenres)
            }catch (e: IOException) {
                movieUiState = MovieUiState.Error
            } catch (e: retrofit2.HttpException) {
                movieUiState = MovieUiState.Error
            }

        }
    }


    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer{
                val application = (this[APPLICATION_KEY] as MovieShowNowApplication)
                val movieRepository = application.container.movieRepository
                MovieViewModel(movieRepository)

            }

        }
    }


}