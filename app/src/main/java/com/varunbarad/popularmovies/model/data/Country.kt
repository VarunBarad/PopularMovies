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
data class Country(
    @Json(name = "iso_3166_1") val isoName: String,
    @Json(name = "name") val name: String
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(Country::class.java).toJson(this)
    }
}
