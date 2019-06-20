package com.varunbarad.popularmovies.external_services.movie_db_api

import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiMovieDetails
import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiMovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbApiService {
    @GET(Constants.DiscoverMovies.GET_POPULAR_MOVIES)
    fun getPopularMovies(@Query("page") page: Long): Call<ApiMovieList>

    @GET(Constants.DiscoverMovies.GET_HIGHEST_RATED_MOVIES)
    fun getHighestRatedMovies(@Query("page") page: Long): Call<ApiMovieList>

    @GET(Constants.Movie.GET_MOVIE_DETAILS)
    fun getMovieDetails(@Path("movieId") movieId: Long): Call<ApiMovieDetails>
}
