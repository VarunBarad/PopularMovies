package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.data.Image

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ImageDb(
    @Json(name = "file_path") val imagePath: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "vote_average") val averageVotes: Double,
    @Json(name = "vote_count") val numberOfVotes: Long
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(ImageDb::class.java).toJson(this)
    }

    fun toImage(): Image {
        return Image(
            imagePath = imagePath,
            width = width,
            height = height,
            averageVotes = averageVotes,
            numberOfVotes = numberOfVotes
        )
    }
}

fun Image.toImageDb(): ImageDb {
    return ImageDb(
        imagePath = imagePath,
        width = width,
        height = height,
        averageVotes = averageVotes,
        numberOfVotes = numberOfVotes
    )
}
