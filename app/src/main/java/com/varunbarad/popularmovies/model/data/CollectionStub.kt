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
data class CollectionStub(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String = "",
    @Json(name = "poster_path") val posterPath: String = "",
    @Json(name = "backdrop_path") val backdropPath: String = ""
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(CollectionStub::class.java).toJson(this)
    }
}
