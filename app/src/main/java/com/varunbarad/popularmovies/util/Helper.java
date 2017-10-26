package com.varunbarad.popularmovies.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.gson.Gson;
import com.varunbarad.popularmovies.model.data.CollectionStub;
import com.varunbarad.popularmovies.model.data.CompanyStub;
import com.varunbarad.popularmovies.model.data.Country;
import com.varunbarad.popularmovies.model.data.Genre;
import com.varunbarad.popularmovies.model.data.ImageList;
import com.varunbarad.popularmovies.model.data.Language;
import com.varunbarad.popularmovies.model.data.MovieDetails;
import com.varunbarad.popularmovies.model.data.MovieList;
import com.varunbarad.popularmovies.model.data.MovieStub;
import com.varunbarad.popularmovies.model.data.ReviewList;
import com.varunbarad.popularmovies.model.data.VideoList;
import com.varunbarad.popularmovies.util.data.MovieContract;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creator: vbarad
 * Date: 2016-12-04
 * Project: PopularMovies
 */

public final class Helper {
  public static int getScreenWidth(View view) {
    int screenSize = view.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    int width;
    if (view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          width = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          width = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          width = 480;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          width = 720;
          break;
        default:
          width = 480;
          break;
      }
    } else {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          width = 426;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          width = 470;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          width = 640;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          width = 960;
          break;
        default:
          width = 640;
          break;
      }
    }
    return width;
  }
  
  public static int getScreenHeight(View view) {
    int screenSize = view.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    int height;
    if (view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          height = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          height = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          height = 480;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          height = 720;
          break;
        default:
          height = 480;
          break;
      }
    } else {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          height = 426;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          height = 470;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          height = 640;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          height = 960;
          break;
        default:
          height = 640;
          break;
      }
    }
    return height;
  }
  
  public static boolean isConnectedToInternet(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    
    boolean isConnected;
    
    isConnected = (activeNetwork != null) && activeNetwork.isConnected();
    
    return isConnected;
  }
  
  public static int convertDpToPx(Context context, int dp) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    int px = Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return px;
  }
  
  public static int convertPxToDp(Context context, int px) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    int dp = Math.round(px / (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return dp;
  }
  
  public static void openUrlInBrowser(String url, Context context) {
    Uri websiteUri = Uri.parse(url);
    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, websiteUri);
    if (websiteIntent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(websiteIntent);
    }
  }
  
  public static void openYouTubeVideo(String videoUrl, Context context) {
    Uri videoUri = Uri.parse(videoUrl);
    Intent videoIntent = new Intent(Intent.ACTION_VIEW, videoUri);
    if (videoIntent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(videoIntent);
    }
  }
  
  public static MovieStub movieStubFromMovieDetails(MovieDetails movieDetails) {
    return new Gson().fromJson(movieDetails.toString(), MovieStub.class);
  }
  
  public static MovieDetails readOneMovie(Cursor cursor) {
    Gson gson = new Gson();
    
    long movieId = cursor.getLong(cursor.getColumnIndex(MovieContract.Movie.COLUMN_MOVIE_ID));
    boolean isAdult = (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_ADULT)) == 1);
    String backdropPath = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_BACKDROP_PATH));
    CollectionStub collection = gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_COLLECTION)), CollectionStub.class);
    long budget = cursor.getLong(cursor.getColumnIndex(MovieContract.Movie.COLUMN_BUDGET));
    ArrayList<Genre> genres = new ArrayList<>(Arrays.asList(gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_GENRES)), Genre[].class)));
    String homepage = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_HOMEPAGE));
    String imdbId = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_IMDB_ID));
    String originalLanguage = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_ORIGINAL_LANGUAGE));
    String originalTitle = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_ORIGINAL_TITLE));
    String overview = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_OVERVIEW));
    double popularity = cursor.getDouble(cursor.getColumnIndex(MovieContract.Movie.COLUMN_POPULARITY));
    String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_POSTER_PATH));
    ArrayList<CompanyStub> productionCompanies = new ArrayList<>(Arrays.asList(gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_PRODUCTION_COMPANIES)), CompanyStub[].class)));
    ArrayList<Country> productionCountries = new ArrayList<>(Arrays.asList(gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_PRODUCTION_COUNTRIES)), Country[].class)));
    String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_RELEASE_DATE));
    long revenue = cursor.getLong(cursor.getColumnIndex(MovieContract.Movie.COLUMN_REVENUE));
    int runtime = cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_RUNTIME));
    ArrayList<Language> spokenLanguages = new ArrayList<>(Arrays.asList(gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_SPOKEN_LANGUAGES)), Language[].class)));
    String status = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_STATUS));
    String tagline = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_TAGLINE));
    String title = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_TITLE));
    boolean isVideo = (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_IS_VIDEO)) == 1);
    double voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieContract.Movie.COLUMN_VOTE_AVERAGE));
    long numberOfVotes = cursor.getLong(cursor.getColumnIndex(MovieContract.Movie.COLUMN_VOTE_COUNT));
    VideoList videos = gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_VIDEOS)), VideoList.class);
    ImageList images = gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_IMAGES)), ImageList.class);
    ReviewList reviews = gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_REVIEWS)), ReviewList.class);
    MovieList recommendations = gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_RECOMMENDED_MOVIES)), MovieList.class);
    MovieList similarMovies = gson.fromJson(cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_SIMILAR_MOVIES)), MovieList.class);
    
    return new MovieDetails(
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
    );
  }
}
