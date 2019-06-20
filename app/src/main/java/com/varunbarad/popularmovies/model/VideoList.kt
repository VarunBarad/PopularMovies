package com.varunbarad.popularmovies.model

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiVideoList

data class VideoList(
    val results: List<Video>
)

fun ApiVideoList.toVideoList(): VideoList {
    return VideoList(
        results = this.results.map { it.toVideo() }
    )
}
