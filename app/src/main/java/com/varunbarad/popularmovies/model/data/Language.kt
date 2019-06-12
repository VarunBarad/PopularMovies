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
data class Language(
    @Json(name = "iso_639_1") val isoCode: String,
    @Json(name = "name") val name: String
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(Language::class.java).toJson(this)
    }
}
