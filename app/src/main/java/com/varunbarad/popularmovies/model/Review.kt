package com.varunbarad.popularmovies.model

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiReview

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
private const val SHORTENED_LENGTH = 200
private const val ellipsis = "…"

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val url: String
) {
    fun getShortenedContent(): String {
        return if (this.content.length > SHORTENED_LENGTH) {
            return "${this.content.substring(0, (SHORTENED_LENGTH - ellipsis.length))}$ellipsis"
        } else {
            this.content
        }
    }
}

fun ApiReview.toReview(): Review {
    return Review(
        id = this.id,
        author = this.author,
        content = this.content,
        url = this.url
    )
}
