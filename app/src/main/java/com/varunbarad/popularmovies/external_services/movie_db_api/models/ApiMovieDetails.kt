package com.varunbarad.popularmovies.external_services.movie_db_api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Creator: Varun Barad
 * Date: 2019-06-20
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class ApiMovieDetails(
    @Json(name = "adult") val isAdult: Boolean = false,
    @Json(name = "backdrop_path") val backdropPath: String = "",
    @Json(name = "genres") val genres: List<ApiGenre> = emptyList(),
    @Json(name = "homepage") val homepage: String = "",
    @Json(name = "id") val id: Long,
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "release_date") val releaseDate: String = "",
    @Json(name = "runtime") val runtime: Int = 0,
    @Json(name = "status") val status: String = "",
    @Json(name = "tagline") val tagLine: String,
    @Json(name = "title") val title: String,
    @Json(name = "vote_average") val averageVote: Double,
    @Json(name = "vote_count") val numberOfVotes: Long,
    @Json(name = "videos") val videos: ApiVideoList? = null,
    @Json(name = "images") val images: ApiImageList? = null,
    @Json(name = "reviews") val reviews: ApiReviewList? = null,
    @Json(name = "recommendations") val recommendations: ApiMovieList? = null,
    @Json(name = "similar_movies") val similarMovies: ApiMovieList? = null
)
