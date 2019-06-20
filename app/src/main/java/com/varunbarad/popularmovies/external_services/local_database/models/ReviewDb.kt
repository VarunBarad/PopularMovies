package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.Review

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ReviewDb(
    @Json(name = "id") val id: String,
    @Json(name = "author") val author: String,
    @Json(name = "content") val content: String,
    @Json(name = "url") val url: String
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(ReviewDb::class.java).toJson(this)
    }

    fun toReview(): Review {
        return Review(
            id = id,
            author = author,
            content = content,
            url = url
        )
    }
}

fun Review.toReviewDb(): ReviewDb {
    return ReviewDb(
        id = id,
        author = author,
        content = content,
        url = url
    )
}
