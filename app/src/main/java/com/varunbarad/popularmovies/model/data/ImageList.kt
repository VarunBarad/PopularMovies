package com.varunbarad.popularmovies.model.data

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiImageList

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
data class ImageList(
    val backdrops: List<Image>,
    val posters: List<Image>
)

fun ApiImageList.toImageList(): ImageList {
    return ImageList(
        backdrops = this.backdrops.map { it.toImage() },
        posters = this.posters.map { it.toImage() }
    )
}
