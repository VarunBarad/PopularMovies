@file:JvmName("MovieCursorHelper")

package com.varunbarad.popularmovies.util

import android.database.Cursor
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.varunbarad.popularmovies.model.data.*
import com.varunbarad.popularmovies.util.data.MovieContract

/**
 * Creator: Varun Barad
 * Date: 2019-06-12
 * Project: PopularMovies
 */
fun Cursor.readOneMovie(): MovieDetails {
    val moshi = Moshi.Builder().build()

    val movieId: Long = this.getLong(this.getColumnIndex(MovieContract.Movie.COLUMN_MOVIE_ID))
    val isAdult: Boolean = (this.getInt(this.getColumnIndex(MovieContract.Movie.COLUMN_ADULT)) == 1)
    val backdropPath: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_BACKDROP_PATH))
    val collection: CollectionStub =
        moshi.adapter(CollectionStub::class.java).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_COLLECTION)))!!
    val budget: Long = this.getLong(this.getColumnIndex(MovieContract.Movie.COLUMN_BUDGET))
    val genres: List<Genre> = moshi.adapter<List<Genre>>(
        Types.newParameterizedType(
            List::class.java,
            Genre::class.java
        )
    ).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_GENRES)))!!
    val homepage: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_HOMEPAGE))
    val imdbId: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_IMDB_ID))
    val originalLanguage: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_ORIGINAL_LANGUAGE))
    val originalTitle: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_ORIGINAL_TITLE))
    val overview: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_OVERVIEW))
    val popularity: Double = this.getDouble(this.getColumnIndex(MovieContract.Movie.COLUMN_POPULARITY))
    val posterPath: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_POSTER_PATH))
    val productionCompanies: List<CompanyStub> = moshi.adapter<List<CompanyStub>>(
        Types.newParameterizedType(
            List::class.java,
            CompanyStub::class.java
        )
    ).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_PRODUCTION_COMPANIES)))!!
    val productionCountries: List<Country> = moshi.adapter<List<Country>>(
        Types.newParameterizedType(
            List::class.java,
            Country::class.java
        )
    ).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_PRODUCTION_COUNTRIES)))!!
    val releaseDate: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_RELEASE_DATE))
    val revenue: Long = this.getLong(this.getColumnIndex(MovieContract.Movie.COLUMN_REVENUE))
    val runtime: Int = this.getInt(this.getColumnIndex(MovieContract.Movie.COLUMN_RUNTIME))
    val spokenLanguages: List<Language> = moshi.adapter<List<Language>>(
        Types.newParameterizedType(
            List::class.java,
            Language::class.java
        )
    ).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_SPOKEN_LANGUAGES)))!!
    val status: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_STATUS))
    val tagline: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_TAGLINE))
    val title: String = this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_TITLE))
    val isVideo: Boolean = (this.getInt(this.getColumnIndex(MovieContract.Movie.COLUMN_IS_VIDEO)) == 1)
    val voteAverage: Double = this.getDouble(this.getColumnIndex(MovieContract.Movie.COLUMN_VOTE_AVERAGE))
    val numberOfVotes: Long = this.getLong(this.getColumnIndex(MovieContract.Movie.COLUMN_VOTE_COUNT))
    val videos: VideoList =
        moshi.adapter(VideoList::class.java).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_VIDEOS)))!!
    val images: ImageList =
        moshi.adapter(ImageList::class.java).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_IMAGES)))!!
    val reviews: ReviewList =
        moshi.adapter(ReviewList::class.java).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_REVIEWS)))!!
    val recommendations: MovieList =
        moshi.adapter(MovieList::class.java).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_RECOMMENDED_MOVIES)))!!
    val similarMovies: MovieList =
        moshi.adapter(MovieList::class.java).fromJson(this.getString(this.getColumnIndex(MovieContract.Movie.COLUMN_SIMILAR_MOVIES)))!!

    return MovieDetails(
        isAdult,
        backdropPath,
        collection,
        budget,
        genres,
        homepage,
        movieId,
        imdbId,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        productionCompanies,
        productionCountries,
        releaseDate,
        revenue,
        runtime,
        spokenLanguages,
        status,
        tagline,
        title,
        isVideo,
        voteAverage,
        numberOfVotes,
        videos,
        images,
        reviews,
        recommendations,
        similarMovies
    )
}
