package com.varunbarad.popularmovies.model

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiMovieList

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
data class MovieList(
    val page: Long,
    val results: List<MovieStub> = emptyList(),
    val totalResults: Long,
    val totalPages: Long
)

fun ApiMovieList.toMovieList(): MovieList {
    return MovieList(
        page = this.page,
        results = this.results.map { it.toMovieStub() },
        totalResults = this.totalResults,
        totalPages = this.totalPages
    )
}
