package com.varunbarad.popularmovies.external_services.movie_db_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ApiMovieStub(
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "adult") val adult: Boolean = false,
    @Json(name = "overview") val overview: String = "",
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "backdrop_path") val backdropPath: String = "",
    @Json(name = "vote_average") val voteAverage: Double
)
