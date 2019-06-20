package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.MovieStub

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class MovieStubDb(
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "adult") val adult: Boolean = false,
    @Json(name = "overview") val overview: String = "",
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "backdrop_path") val backdropPath: String = "",
    @Json(name = "vote_average") val voteAverage: Double
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(MovieStubDb::class.java).toJson(this)
    }

    fun toMovieStub(): MovieStub {
        return MovieStub(
            posterPath = posterPath,
            adult = adult,
            overview = overview,
            releaseDate = releaseDate,
            id = id,
            title = title,
            backdropPath = backdropPath,
            voteAverage = voteAverage
        )
    }
}

fun MovieStub.toMovieStubDb(): MovieStubDb {
    return MovieStubDb(
        posterPath = posterPath,
        adult = adult,
        overview = overview,
        releaseDate = releaseDate,
        id = id,
        title = title,
        backdropPath = backdropPath,
        voteAverage = voteAverage
    )
}
