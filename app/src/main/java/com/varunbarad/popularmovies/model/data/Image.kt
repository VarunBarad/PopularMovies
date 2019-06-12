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
data class Image(
    @Json(name = "file_path") val imagePath: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "vote_average") val averageVotes: Double,
    @Json(name = "vote_count") val numberOfVotes: Long
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(Image::class.java).toJson(this)
    }
}
