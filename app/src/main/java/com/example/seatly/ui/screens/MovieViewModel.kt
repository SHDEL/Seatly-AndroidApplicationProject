package com.example.seatly.ui.screens


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
import com.example.seatly.model.Movie
import kotlinx.coroutines.launch
import java.io.IOException


sealed interface MovieUiState {
    data class Success(val movies: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}


class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel(){
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    init {
        getMovieNowShow()
    }

    fun getMovieNowShow() {
        viewModelScope.launch {
            movieUiState = MovieUiState.Loading
            movieUiState = try {
                MovieUiState.Success(movieRepository.getMovies())
            } catch (e: IOException) {
                MovieUiState.Error
            } catch (e: retrofit2.HttpException) {
                MovieUiState.Error
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