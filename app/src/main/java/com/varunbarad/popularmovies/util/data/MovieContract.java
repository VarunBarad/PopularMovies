package com.varunbarad.popularmovies.util.data;

import android.net.Uri;

/**
 * Creator: Varun Barad
 * Date: 23-10-2017
 * Project: PopularMovies
 */
public final class MovieContract {
  public static final String CONTENT_AUTHORITY = "com.varunbarad.popularmovies";
  
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
  
  public static final String PATH_MOVIE = "movie";
  
  public static final String PATH_FAVORITES = "favorites";
  
  public static final class Movie {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
        .appendPath(PATH_MOVIE)
        .build();
    
    public static final Uri FAVORITES_URI = BASE_CONTENT_URI.buildUpon()
        .appendPath(PATH_FAVORITES)
        .build();
    
    public static final String TABLE_NAME = "movie";
    
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_ADULT = "adult";
    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_COLLECTION = "collection";
    public static final String COLUMN_BUDGET = "budget";
    public static final String COLUMN_GENRES = "genre";
    public static final String COLUMN_HOMEPAGE = "homepage";
    public static final String COLUMN_IMDB_ID = "imdb_id";
    public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_PRODUCTION_COMPANIES = "production_company";
    public static final String COLUMN_PRODUCTION_COUNTRIES = "production_countries";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_REVENUE = "revenue";
    public static final String COLUMN_RUNTIME = "runtime";
    public static final String COLUMN_SPOKEN_LANGUAGES = "spoken_languages";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TAGLINE = "tagline";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IS_VIDEO = "is_video";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_VOTE_COUNT = "vote_count";
    public static final String COLUMN_VIDEOS = "videos";
    public static final String COLUMN_IMAGES = "images";
    public static final String COLUMN_REVIEWS = "reviews";
    public static final String COLUMN_RECOMMENDED_MOVIES = "recommended_movies";
    public static final String COLUMN_SIMILAR_MOVIES = "similar_movies";
    public static final String COLUMN_IS_FAVORITE = "is_favorite";
    
    public static Uri buildUriWithMovieId(long movieId) {
      return CONTENT_URI.buildUpon()
          .appendPath(String.valueOf(movieId))
          .build();
    }
  }
}
