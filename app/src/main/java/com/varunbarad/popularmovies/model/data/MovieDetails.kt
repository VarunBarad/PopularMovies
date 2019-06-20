package com.varunbarad.popularmovies.model.data

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiMovieDetails

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
data class MovieDetails(
    val isAdult: Boolean = false,
    val backdropPath: String = "",
    val genres: List<Genre> = emptyList(),
    val homepage: String = "",
    val id: Long,
    val overview: String,
    val posterPath: String,
    val releaseDate: String = "",
    val runtime: Int = 0,
    val status: String = "",
    val tagLine: String,
    val title: String,
    val averageVote: Double,
    val numberOfVotes: Long,
    val videos: VideoList? = null,
    val images: ImageList? = null,
    val reviews: ReviewList? = null,
    val recommendations: MovieList? = null,
    val similarMovies: MovieList? = null
) {
    fun toMovieStub(): MovieStub {
        return MovieStub(
            posterPath = this.posterPath,
            adult = this.isAdult,
            overview = this.overview,
            releaseDate = this.releaseDate,
            id = this.id,
            title = this.title,
            backdropPath = this.backdropPath,
            voteAverage = this.averageVote
        )
    }
}

fun ApiMovieDetails.toMovieDetails(): MovieDetails {
    return MovieDetails(
        isAdult = this.isAdult,
        backdropPath = this.backdropPath,
        genres = this.genres.map { it.toGenre() },
        homepage = this.homepage,
        id = this.id,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        runtime = this.runtime,
        status = this.status,
        tagLine = this.tagLine,
        title = this.title,
        averageVote = this.averageVote,
        numberOfVotes = this.numberOfVotes,
        videos = this.videos?.toVideoList(),
        images = this.images?.toImageList(),
        reviews = this.reviews?.toReviewList(),
        recommendations = this.recommendations?.toMovieList(),
        similarMovies = this.similarMovies?.toMovieList()
    )
}
