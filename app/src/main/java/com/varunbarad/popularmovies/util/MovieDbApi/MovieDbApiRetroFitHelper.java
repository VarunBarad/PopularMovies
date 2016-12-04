package com.varunbarad.popularmovies.util.MovieDbApi;

import com.varunbarad.popularmovies.model.data.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Creator: vbarad
 * Date: 2016-11-24
 * Project: PopularMovies
 */

public interface MovieDbApiRetroFitHelper {
  String baseUrl = "https://api.themoviedb.org/";

  @GET("/3/discover/movie?sort_by=popularity.desc&vote_count.gte=50")
  Call<MovieList> getPopularMovies(@Query("page") long page, @Query("api_key") String apiKey);
}
