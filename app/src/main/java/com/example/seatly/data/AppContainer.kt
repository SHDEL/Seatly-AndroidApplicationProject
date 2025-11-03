package com.example.seatly.data

import com.example.seatly.BuildConfig
import com.example.seatly.network.MovieApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val movieRepository: MovieRepository
}

private const val API_KEY = BuildConfig.API_KEY
private const val API_URL = BuildConfig.API_URL

class DefaultAppContainer: AppContainer{

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $API_KEY")
                .build()
            chain.proceed(request)
        }
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(API_URL)
        .client(client)
        .build()

    private val retrofitService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }

    override val movieRepository: MovieRepository by lazy {
        NetworkMovieRepository(retrofitService)
    }

}