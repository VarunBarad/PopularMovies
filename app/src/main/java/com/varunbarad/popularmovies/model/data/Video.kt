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
data class Video(
    @Json(name = "id") val id: String,
    @Json(name = "iso_639_1") val isoLanguageCode: String,
    @Json(name = "iso_3166_1") val isoCountryCode: String,
    @Json(name = "key") val key: String,
    @Json(name = "name") val name: String,
    @Json(name = "site") val site: String,
    @Json(name = "size") val size: Int,
    @Json(name = "type") val type: String
) {
    fun getVideoUrl(): String? {
        return if (this.site.toLowerCase() == "youtube") {
            "https://www.youtube.com/watch?v=${this.key}"
        } else {
            null
        }
    }

    override fun toString(): String {
        return Moshi.Builder().build().adapter(Video::class.java).toJson(this)
    }
}
