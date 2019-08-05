package com.varunbarad.popularmovies.external_services.movie_db_api

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiMovieDetails
import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiMovieList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApiService {
    @GET(Constants.DiscoverMovies.GET_POPULAR_MOVIES)
    fun getPopularMovies(@Query("page") page: Long): Single<ApiMovieList>

    @GET(Constants.DiscoverMovies.GET_HIGHEST_RATED_MOVIES)
    fun getHighestRatedMovies(@Query("page") page: Long): Single<ApiMovieList>

    @GET(Constants.Movie.GET_MOVIE_DETAILS)
    fun getMovieDetails(@Path("movieId") movieId: Long): Single<ApiMovieDetails>
}
