package com.varunbarad.popularmovies.external_services.movie_db_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ApiReviewList(
    @Json(name = "page") val page: Long,
    @Json(name = "results") val results: List<ApiReview>,
    @Json(name = "total_pages") val totalPages: Long,
    @Json(name = "total_results") val totalResults: Long
)
