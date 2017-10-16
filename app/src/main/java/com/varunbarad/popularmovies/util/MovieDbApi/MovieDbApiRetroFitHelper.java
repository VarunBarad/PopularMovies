package com.varunbarad.popularmovies.util.MovieDbApi;

import com.varunbarad.popularmovies.BuildConfig;
import com.varunbarad.popularmovies.model.data.MovieDetails;
import com.varunbarad.popularmovies.model.data.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Creator: vbarad
 * Date: 2016-11-24
 * Project: PopularMovies
 */

public interface MovieDbApiRetroFitHelper {
  String baseUrl = "https://api.themoviedb.org/";

  @GET("/3/discover/movie?api_key=" + BuildConfig.TMDBApiKey + "&sort_by=popularity.desc&vote_count.gte=50")
  Call<MovieList> getPopularMovies(@Query("page") long page);
  
  @GET("/3/movie/{movieId}?api_key=" + BuildConfig.TMDBApiKey + "&append_to_response=videos,images,reviews,recommendations,similar_movies")
  Call<MovieDetails> getMovieDetails(@Path("movieId") long movieId);
}
