package com.varunbarad.popularmovies.model.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import java.io.IOException

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class MovieDetails(
    @Json(name = "adult") val isAdult: Boolean = false,
    @Json(name = "backdrop_path") val backdropPath: String = "",
    @Json(name = "genres") val genres: List<Genre> = emptyList(),
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
    @Json(name = "videos") val videos: VideoList? = null,
    @Json(name = "images") val images: ImageList? = null,
    @Json(name = "reviews") val reviews: ReviewList? = null,
    @Json(name = "recommendations") val recommendations: MovieList? = null,
    @Json(name = "similar_movies") val similarMovies: MovieList? = null
) {
    @Throws(IOException::class)
    fun toMovieStub(): MovieStub {
        return Moshi.Builder().build().adapter(MovieStub::class.java).fromJson(this.toString())!!
    }

    override fun toString(): String {
        return Moshi.Builder().build().adapter(MovieDetails::class.java).toJson(this)
    }
}
