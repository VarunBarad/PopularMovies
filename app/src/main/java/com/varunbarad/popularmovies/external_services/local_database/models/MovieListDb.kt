package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.MovieList

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class MovieListDb(
    @Json(name = "page") val page: Long,
    @Json(name = "results") val results: List<MovieStubDb> = emptyList(),
    @Json(name = "total_results") val totalResults: Long,
    @Json(name = "total_pages") val totalPages: Long
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(MovieListDb::class.java).toJson(this)
    }

    fun toMovieList(): MovieList {
        return MovieList(
            page = this.page,
            results = this.results.map { it.toMovieStub() },
            totalResults = this.totalResults,
            totalPages = this.totalPages
        )
    }
}

fun MovieList.toMovieListDb(): MovieListDb {
    return MovieListDb(
        page = this.page,
        results = this.results.map { it.toMovieStubDb() },
        totalResults = this.totalResults,
        totalPages = this.totalPages
    )
}
