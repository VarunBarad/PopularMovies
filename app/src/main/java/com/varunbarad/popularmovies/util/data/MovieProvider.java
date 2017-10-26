package com.varunbarad.popularmovies.util.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Creator: Varun Barad
 * Date: 25-10-2017
 * Project: PopularMovies
 */
public class MovieProvider extends ContentProvider {
  public static final int CODE_MOVIE_FAVORITES = 101;
  public static final int CODE_MOVIE_DETAIL = 102;
  private static final UriMatcher uriMatcher = MovieProvider.buildUriMatcher();
  private MovieDbHelper databaseHelper;
  
  private static UriMatcher buildUriMatcher() {
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    final String authority = MovieContract.CONTENT_AUTHORITY;
    
    matcher.addURI(authority, MovieContract.PATH_FAVORITES, CODE_MOVIE_FAVORITES);
    matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_DETAIL);
    
    return matcher;
  }
  
  @Override
  public boolean onCreate() {
    this.databaseHelper = new MovieDbHelper(this.getContext());
    return true;
  }
  
  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    Cursor cursor;
    
    switch (MovieProvider.uriMatcher.match(uri)) {
      case MovieProvider.CODE_MOVIE_FAVORITES: {
        cursor = this.databaseHelper.getReadableDatabase().query(
            MovieContract.Movie.TABLE_NAME,
            projection,
            MovieContract.Movie.COLUMN_IS_FAVORITE + " = 1",
            null,
            null,
            null,
            null
        );
        
        break;
      }
      case MovieProvider.CODE_MOVIE_DETAIL: {
        String movieId = uri.getLastPathSegment();
        
        String[] movieIdArguments = new String[]{movieId};
        
        cursor = this.databaseHelper.getReadableDatabase().query(
            MovieContract.Movie.TABLE_NAME,
            null,
            MovieContract.Movie.COLUMN_MOVIE_ID + " = ?",
            movieIdArguments,
            null,
            null,
            null
        );
        
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    
    cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
    return cursor;
  }
  
  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    throw new RuntimeException("getType is not implemented in Pop Movies.");
  }
  
  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    final SQLiteDatabase database = this.databaseHelper.getWritableDatabase();
    
    switch (MovieProvider.uriMatcher.match(uri)) {
      case MovieProvider.CODE_MOVIE_DETAIL: {
        database.insert(
            MovieContract.Movie.TABLE_NAME,
            null,
            values
        );
        
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    
    return uri;
  }
  
  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    throw new UnsupportedOperationException("Delete is not supported on this provider");
  }
  
  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
    int numberOfRowsUpdated = 0;
    
    switch (MovieProvider.uriMatcher.match(uri)) {
      case MovieProvider.CODE_MOVIE_DETAIL:
        String movieId = uri.getLastPathSegment();
        
        numberOfRowsUpdated = this.databaseHelper.getWritableDatabase().update(
            MovieContract.Movie.TABLE_NAME,
            values,
            MovieContract.Movie.COLUMN_MOVIE_ID + " = ?",
            new String[]{movieId}
        );
        
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    
    return numberOfRowsUpdated;
  }
}
