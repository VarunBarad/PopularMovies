package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.data.ReviewList

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ReviewListDb(
    @Json(name = "page") val page: Long,
    @Json(name = "results") val results: List<ReviewDb>,
    @Json(name = "total_pages") val totalPages: Long,
    @Json(name = "total_results") val totalResults: Long
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(ReviewListDb::class.java).toJson(this)
    }

    fun toReviewList(): ReviewList {
        return ReviewList(
            page = this.page,
            results = this.results.map { it.toReview() },
            totalPages = this.totalPages,
            totalResults = this.totalResults
        )
    }
}

fun ReviewList.toReviewListDb(): ReviewListDb {
    return ReviewListDb(
        page = this.page,
        results = this.results.map { it.toReviewDb() },
        totalPages = this.totalPages,
        totalResults = this.totalResults
    )
}
