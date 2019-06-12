package com.varunbarad.popularmovies.model.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class MovieStub(
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "adult") val adult: Boolean = false,
    @Json(name = "overview") val overview: String = "",
    @Json(name = "release_date") val releaseDate: String? = null,
    @Json(name = "genre_ids") val genreIds: List<Long>,
    @Json(name = "id") val id: Long,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "title") val title: String,
    @Json(name = "backdrop_path") val backdropPath: String = "",
    @Json(name = "popularity") val popularity: Double,
    @Json(name = "vote_count") val voteCount: Long,
    @Json(name = "video") val video: Boolean = false,
    @Json(name = "vote_average") val voteAverage: Double
) {
    override fun toString(): String {
        return Moshi.Builder().build().adapter(MovieStub::class.java).toJson(this)
    }

    companion object {
        @JvmStatic
        fun getInstance(movieStubJson: String): MovieStub? {
            return Moshi.Builder().build().adapter(MovieStub::class.java).fromJson(movieStubJson)
        }
    }
}
