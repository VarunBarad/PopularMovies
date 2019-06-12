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
data class ImageList(
    @Json(name = "backdrops") val backdrops: List<Image>,
    @Json(name = "posters") val posters: List<Image>
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(ImageList::class.java).toJson(this)
    }
}
