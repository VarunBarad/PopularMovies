package com.varunbarad.popularmovies.external_services.movie_db_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ApiImageList(
    @Json(name = "backdrops") val backdrops: List<ApiImage>,
    @Json(name = "posters") val posters: List<ApiImage>
)
