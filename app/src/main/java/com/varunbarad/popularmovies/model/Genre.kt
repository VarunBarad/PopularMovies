package com.varunbarad.popularmovies.model

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiGenre

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
data class Genre(
    val id: Long,
    val name: String
)

fun ApiGenre.toGenre(): Genre {
    return Genre(
        id = this.id,
        name = this.name
    )
}
