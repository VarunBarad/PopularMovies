package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.VideoList

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class VideoListDb(
    @Json(name = "results") val results: List<VideoDb>
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(VideoListDb::class.java).toJson(this)
    }

    fun toVideoList(): VideoList {
        return VideoList(
            results = this.results.map { it.toVideo() }
        )
    }
}

fun VideoList.toVideoListDb(): VideoListDb {
    return VideoListDb(
        results = this.results.map { it.toVideoDb() }
    )
}
