package com.varunbarad.popularmovies.external_services.local_database.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.varunbarad.popularmovies.model.Video

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class VideoDb(
    @Json(name = "id") val id: String,
    @Json(name = "iso_639_1") val isoLanguageCode: String,
    @Json(name = "iso_3166_1") val isoCountryCode: String,
    @Json(name = "key") val key: String,
    @Json(name = "name") val name: String,
    @Json(name = "site") val site: String,
    @Json(name = "size") val size: Int,
    @Json(name = "type") val type: String
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(VideoDb::class.java).toJson(this)
    }

    fun toVideo(): Video {
        return Video(
            id = id,
            isoLanguageCode = isoLanguageCode,
            isoCountryCode = isoCountryCode,
            key = key,
            name = name,
            site = site,
            size = size,
            type = type
        )
    }
}

fun Video.toVideoDb(): VideoDb {
    return VideoDb(
        id = id,
        isoLanguageCode = isoLanguageCode,
        isoCountryCode = isoCountryCode,
        key = key,
        name = name,
        site = site,
        size = size,
        type = type
    )
}
