package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.data.Genre

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class GenreDb(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(GenreDb::class.java).toJson(this)
    }

    fun toGenre(): Genre {
        return Genre(
            id = this.id,
            name = this.name
        )
    }
}

fun Genre.toGenreDb(): GenreDb {
    return GenreDb(
        id = this.id,
        name = this.name
    )
}
