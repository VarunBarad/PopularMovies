package com.varunbarad.popularmovies.model

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiReviewList

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
data class ReviewList(
    val page: Long,
    val results: List<Review>,
    val totalPages: Long,
    val totalResults: Long
)

fun ApiReviewList.toReviewList(): ReviewList {
    return ReviewList(
        page = this.page,
        results = this.results.map { it.toReview() },
        totalPages = this.totalPages,
        totalResults = this.totalResults
    )
}
