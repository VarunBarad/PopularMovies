package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.ImageList

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ImageListDb(
    @Json(name = "backdrops") val backdrops: List<ImageDb>,
    @Json(name = "posters") val posters: List<ImageDb>
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(ImageListDb::class.java).toJson(this)
    }

    fun toImageList(): ImageList {
        return ImageList(
            backdrops = this.backdrops.map { it.toImage() },
            posters = this.posters.map { it.toImage() }
        )
    }
}

fun ImageList.toImageListDb(): ImageListDb {
    return ImageListDb(
        backdrops = this.backdrops.map { it.toImageDb() },
        posters = this.posters.map { it.toImageDb() }
    )
}
