package com.varunbarad.popularmovies.model.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
private const val SHORTENED_LENGTH = 200
private const val ellipsis = "…"

@JsonClass(generateAdapter = true)
data class Review(
    @Json(name = "id") val id: String,
    @Json(name = "author") val author: String,
    @Json(name = "content") val content: String,
    @Json(name = "url") val url: String
) {
    fun getShortenedContent(): String {
        return if (this.content.length > SHORTENED_LENGTH) {
            return "${this.content.substring(0, (SHORTENED_LENGTH - ellipsis.length))}$ellipsis"
        } else {
            this.content
        }
    }

    override fun toString(): String {
        return Moshi.Builder().build().adapter(Review::class.java).toJson(this)
    }
}
