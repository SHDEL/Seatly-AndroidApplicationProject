package com.example.seatly.network
import com.example.seatly.model.MovieResponse
import retrofit2.http.GET



interface MovieApiService {
    @GET("movie/now_playing")
    suspend fun getNowPlaying(): MovieResponse
}
