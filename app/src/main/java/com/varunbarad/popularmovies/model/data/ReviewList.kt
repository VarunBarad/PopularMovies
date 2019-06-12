package com.varunbarad.popularmovies.model.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ReviewList(
    @Json(name = "page") val page: Long,
    @Json(name = "results") val results: List<Review>,
    @Json(name = "total_pages") val totalPages: Long,
    @Json(name = "total_results") val totalResults: Long
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(ReviewList::class.java).toJson(this)
    }
}
