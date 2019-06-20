package com.varunbarad.popularmovies.model

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiVideo

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
data class Video(
    val id: String,
    val isoLanguageCode: String,
    val isoCountryCode: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
) {
    fun getVideoUrl(): String? {
        return if (this.site.toLowerCase() == "youtube") {
            "https://www.youtube.com/watch?v=${this.key}"
        } else {
            null
        }
    }
}

fun ApiVideo.toVideo(): Video {
    return Video(
        id = this.id,
        isoLanguageCode = this.isoLanguageCode,
        isoCountryCode = this.isoCountryCode,
        key = this.key,
        name = this.name,
        site = this.site,
        size = this.size,
        type = this.type
    )
}
