package com.varunbarad.popularmovies.external_services.movie_db_api

import com.varunbarad.popularmovies.BuildConfig

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/"

    object DiscoverMovies {
        const val GET_POPULAR_MOVIES =
            "/3/discover/movie?api_key=${BuildConfig.TMDBApiKey}&sort_by=popularity.desc&vote_count.gte=50"
        const val GET_HIGHEST_RATED_MOVIES =
            "/3/discover/movie?api_key=${BuildConfig.TMDBApiKey}&sort_by=vote_average.desc&vote_count.gte=50"
    }

    object Movie {
        const val GET_MOVIE_DETAILS =
            "/3/movie/{movieId}?api_key=${BuildConfig.TMDBApiKey}&append_to_response=videos,images,reviews,recommendations,similar_movies"
    }
}
