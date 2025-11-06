package com.example.seatly.data

import com.example.seatly.model.Genre
import com.example.seatly.model.Movie
import com.example.seatly.network.MovieApiService

interface MovieRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getGenres(): List<Genre>
}

class NetworkMovieRepository(private val movieApiService : MovieApiService): MovieRepository{
    override suspend fun getMovies(): List<Movie> {
        return movieApiService.getNowPlaying().results
    }

    override suspend fun getGenres(): List<Genre> {
        return movieApiService.getGenres().genres
    }
}
