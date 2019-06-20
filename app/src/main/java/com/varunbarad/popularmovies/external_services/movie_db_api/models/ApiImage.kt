package com.varunbarad.popularmovies.external_services.movie_db_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ApiImage(
    @Json(name = "file_path") val imagePath: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "vote_average") val averageVotes: Double,
    @Json(name = "vote_count") val numberOfVotes: Long
)
