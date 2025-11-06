package com.example.seatly.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    @SerialName("genre_ids")
    val genre: List<Int> = emptyList(),
    val title: String,
    val overview: String,

    @SerialName("poster_path")
    val poster: String? = null,
    @SerialName("backdrop_path")
    val backdrop: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,


    val genreName: List<String> = emptyList()

)

@Serializable
data class MovieResponse(
    val results: List<Movie>
)