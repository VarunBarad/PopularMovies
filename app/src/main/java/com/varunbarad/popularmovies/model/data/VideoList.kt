package com.varunbarad.popularmovies.model.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

@JsonClass(generateAdapter = true)
data class VideoList(
    @Json(name = "results") val results: List<Video>
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(VideoList::class.java).toJson(this)
    }
}
