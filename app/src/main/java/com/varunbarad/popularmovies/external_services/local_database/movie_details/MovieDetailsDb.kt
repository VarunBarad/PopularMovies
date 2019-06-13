package com.varunbarad.popularmovies.external_services.local_database.movie_details

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.varunbarad.popularmovies.model.data.*

/**
 * Creator: Varun Barad
 * Date: 2019-06-12
 * Project: PopularMovies
 */
@Entity(tableName = "movie")
data class MovieDetailsDb(
    @PrimaryKey @ColumnInfo(name = "movie_id") val id: Long,
    @ColumnInfo(name = "adult") val isAdult: Boolean = false,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String = "",
    @ColumnInfo(name = "genre") val genres: List<Genre> = emptyList(),
    @ColumnInfo(name = "homepage") val homepage: String = "",
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "poster_path") val posterPath: String,
    @ColumnInfo(name = "release_date") val releaseDate: String = "",
    @ColumnInfo(name = "runtime") val runtime: Int = 0,
    @ColumnInfo(name = "status") val status: String = "",
    @ColumnInfo(name = "tagline") val tagLine: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "vote_average") val averageVote: Double,
    @ColumnInfo(name = "vote_count") val numberOfVotes: Long,
    @ColumnInfo(name = "videos") val videos: VideoList? = null,
    @ColumnInfo(name = "images") val images: ImageList? = null,
    @ColumnInfo(name = "reviews") val reviews: ReviewList? = null,
    @ColumnInfo(name = "recommended_movies") val recommendations: MovieList? = null,
    @ColumnInfo(name = "similar_movies") val similarMovies: MovieList? = null,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
) {
    fun toMovieDetails(): MovieDetails {
        return MovieDetails(
            id = this.id,
            isAdult = this.isAdult,
            backdropPath = this.backdropPath,
            genres = this.genres,
            homepage = this.homepage,
            overview = this.overview,
            posterPath = this.posterPath,
            releaseDate = this.releaseDate,
            runtime = this.runtime,
            status = this.status,
            tagLine = this.tagLine,
            title = this.title,
            averageVote = this.averageVote,
            numberOfVotes = this.numberOfVotes,
            videos = this.videos,
            images = this.images,
            reviews = this.reviews,
            recommendations = this.recommendations,
            similarMovies = this.similarMovies
        )
    }
}

fun MovieDetails.toMovieDetailsDb(isFavorite: Boolean): MovieDetailsDb {
    return MovieDetailsDb(
        id = this.id,
        isAdult = this.isAdult,
        backdropPath = this.backdropPath,
        genres = this.genres,
        homepage = this.homepage,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        runtime = this.runtime,
        status = this.status,
        tagLine = this.tagLine,
        title = this.title,
        averageVote = this.averageVote,
        numberOfVotes = this.numberOfVotes,
        videos = this.videos,
        images = this.images,
        reviews = this.reviews,
        recommendations = this.recommendations,
        similarMovies = this.similarMovies,
        isFavorite = isFavorite
    )
}
