@file:JvmName("YouTubeApiHelper")
package com.varunbarad.popularmovies.util

/**
 * Creator: Varun Barad
 * Date: 2019-06-03
 * Project: PopularMovies
 */

fun String.isValidVideoId(): Boolean {
    return this.trim().matches(Regex("[0-9a-zA-Z-_]{11}"))
}

fun getDefaultThumbnailUrl(videoId: String): String? {
    return if (videoId.isValidVideoId()) {
        "https://i.ytimg.com/vi/$videoId/default.jpg"
    } else {
        null
    }
}
