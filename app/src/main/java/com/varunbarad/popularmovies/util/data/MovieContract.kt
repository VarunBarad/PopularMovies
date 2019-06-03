package com.varunbarad.popularmovies.util.data

import android.net.Uri

/**
 * Creator: Varun Barad
 * Date: 2019-06-03
 * Project: PopularMovies
 */
object MovieContract {
    const val CONTENT_AUTHORITY = "com.varunbarad.popularmovies"

    @JvmStatic
    private val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")

    const val PATH_MOVIE = "movie"
    const val PATH_FAVORITES = "favorites"

    object Movie {
        @JvmStatic
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build()

        @JvmStatic
        val FAVORITES_URI: Uri = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build()

        const val TABLE_NAME = "movie";

        const val COLUMN_MOVIE_ID = "movie_id";
        const val COLUMN_ADULT = "adult";
        const val COLUMN_BACKDROP_PATH = "backdrop_path";
        const val COLUMN_COLLECTION = "collection";
        const val COLUMN_BUDGET = "budget";
        const val COLUMN_GENRES = "genre";
        const val COLUMN_HOMEPAGE = "homepage";
        const val COLUMN_IMDB_ID = "imdb_id";
        const val COLUMN_ORIGINAL_LANGUAGE = "original_language";
        const val COLUMN_ORIGINAL_TITLE = "original_title";
        const val COLUMN_OVERVIEW = "overview";
        const val COLUMN_POPULARITY = "popularity";
        const val COLUMN_POSTER_PATH = "poster_path";
        const val COLUMN_PRODUCTION_COMPANIES = "production_company";
        const val COLUMN_PRODUCTION_COUNTRIES = "production_countries";
        const val COLUMN_RELEASE_DATE = "release_date";
        const val COLUMN_REVENUE = "revenue";
        const val COLUMN_RUNTIME = "runtime";
        const val COLUMN_SPOKEN_LANGUAGES = "spoken_languages";
        const val COLUMN_STATUS = "status";
        const val COLUMN_TAGLINE = "tagline";
        const val COLUMN_TITLE = "title";
        const val COLUMN_IS_VIDEO = "is_video";
        const val COLUMN_VOTE_AVERAGE = "vote_average";
        const val COLUMN_VOTE_COUNT = "vote_count";
        const val COLUMN_VIDEOS = "videos";
        const val COLUMN_IMAGES = "images";
        const val COLUMN_REVIEWS = "reviews";
        const val COLUMN_RECOMMENDED_MOVIES = "recommended_movies";
        const val COLUMN_SIMILAR_MOVIES = "similar_movies";
        const  val COLUMN_IS_FAVORITE = "is_favorite";

        @JvmStatic
        fun buildUriWithMovieId(movieId: Long): Uri {
            return CONTENT_URI.buildUpon()
                    .appendPath(movieId.toString())
                    .build()
        }
    }
}
