package com.example.seatly.network
import com.example.seatly.model.GenreResponse
import com.example.seatly.model.MovieResponse
import retrofit2.http.GET



interface MovieApiService {
    @GET("movie/now_playing")
    suspend fun getNowPlaying(): MovieResponse

    @GET("genre/movie/list")
    suspend fun getGenres(): GenreResponse

    @GET("movie/upcoming")
    suspend fun getComingSoon(): MovieResponse


}
