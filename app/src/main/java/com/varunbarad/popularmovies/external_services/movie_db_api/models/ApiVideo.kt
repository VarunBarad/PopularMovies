package com.varunbarad.popularmovies.external_services.movie_db_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ApiVideo(
    @Json(name = "id") val id: String,
    @Json(name = "iso_639_1") val isoLanguageCode: String,
    @Json(name = "iso_3166_1") val isoCountryCode: String,
    @Json(name = "key") val key: String,
    @Json(name = "name") val name: String,
    @Json(name = "site") val site: String,
    @Json(name = "size") val size: Int,
    @Json(name = "type") val type: String
)
