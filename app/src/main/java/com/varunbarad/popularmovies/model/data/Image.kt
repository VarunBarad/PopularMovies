package com.varunbarad.popularmovies.model.data

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiImage

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
data class Image(
    val imagePath: String,
    val width: Int,
    val height: Int,
    val averageVotes: Double,
    val numberOfVotes: Long
)

fun ApiImage.toImage(): Image {
    return Image(
        imagePath = this.imagePath,
        width = this.width,
        height = this.height,
        averageVotes = this.averageVotes,
        numberOfVotes = this.numberOfVotes
    )
}
