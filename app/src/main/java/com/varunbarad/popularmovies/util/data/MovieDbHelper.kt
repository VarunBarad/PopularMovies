package com.varunbarad.popularmovies.util.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.varunbarad.popularmovies.model.data.MovieDetails

/**
 * Creator: Varun Barad
 * Date: 2019-06-03
 * Project: PopularMovies
 */
private const val DATABASE_NAME = "movies.db"
private const val DATABASE_VERSION = 1

class MovieDbHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
  override fun onCreate(db: SQLiteDatabase?) {
    db?.execSQL("""
        CREATE TABLE ${MovieContract.Movie.TABLE_NAME} (
        ${MovieContract.Movie.COLUMN_MOVIE_ID} INTEGER PRIMARY KEY ON CONFLICT REPLACE,
        ${MovieContract.Movie.COLUMN_ADULT} INTEGER NOT NULL,
        ${MovieContract.Movie.COLUMN_BACKDROP_PATH} TEXT,
        ${MovieContract.Movie.COLUMN_COLLECTION} TEXT,
        ${MovieContract.Movie.COLUMN_BUDGET} INTEGER,
        ${MovieContract.Movie.COLUMN_GENRES} TEXT NOT NULL,
        ${MovieContract.Movie.COLUMN_HOMEPAGE} TEXT,
        ${MovieContract.Movie.COLUMN_IMDB_ID} TEXT,
        ${MovieContract.Movie.COLUMN_ORIGINAL_LANGUAGE} TEXT,
        ${MovieContract.Movie.COLUMN_ORIGINAL_TITLE} TEXT,
        ${MovieContract.Movie.COLUMN_OVERVIEW} TEXT,
        ${MovieContract.Movie.COLUMN_POPULARITY} REAL,
        ${MovieContract.Movie.COLUMN_POSTER_PATH} TEXT NOT NULL,
        ${MovieContract.Movie.COLUMN_PRODUCTION_COMPANIES} TEXT,
        ${MovieContract.Movie.COLUMN_PRODUCTION_COUNTRIES} TEXT,
        ${MovieContract.Movie.COLUMN_RELEASE_DATE} TEXT,
        ${MovieContract.Movie.COLUMN_REVENUE} INTEGER,
        ${MovieContract.Movie.COLUMN_RUNTIME} INTEGER,
        ${MovieContract.Movie.COLUMN_SPOKEN_LANGUAGES} TEXT,
        ${MovieContract.Movie.COLUMN_STATUS} TEXT,
        ${MovieContract.Movie.COLUMN_TAGLINE} TEXT,
        ${MovieContract.Movie.COLUMN_TITLE} TEXT NOT NULL,
        ${MovieContract.Movie.COLUMN_IS_VIDEO} INTEGER,
        ${MovieContract.Movie.COLUMN_VOTE_AVERAGE} REAL,
        ${MovieContract.Movie.COLUMN_VOTE_COUNT} INTEGER,
        ${MovieContract.Movie.COLUMN_VIDEOS} TEXT,
        ${MovieContract.Movie.COLUMN_IMAGES} TEXT,
        ${MovieContract.Movie.COLUMN_REVIEWS} TEXT,
        ${MovieContract.Movie.COLUMN_RECOMMENDED_MOVIES} TEXT,
        ${MovieContract.Movie.COLUMN_SIMILAR_MOVIES} TEXT,
        ${MovieContract.Movie.COLUMN_IS_FAVORITE} INTEGER DEFAULT 0
        );
        """.trimIndent())
  }
  
  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db?.execSQL("DROP TABLE IF EXISTS ${MovieContract.Movie.TABLE_NAME};")
    this.onCreate(db)
  }
  
  fun queryMovieDetails(movieId: Long): Cursor? {
    return this.readableDatabase.query(
        MovieContract.Movie.TABLE_NAME,
        null,
        MovieContract.Movie.COLUMN_MOVIE_ID + " = ?",
        arrayOf(movieId.toString()),
        null,
        null,
        null
    )
  }
  
  fun queryFavoriteMovies(): Cursor? {
    return this.readableDatabase.query(
        MovieContract.Movie.TABLE_NAME,
        null,
        "${MovieContract.Movie.COLUMN_IS_FAVORITE} = 1",
        null,
        null,
        null,
        null
    )
  }
  
  fun insertMovieDetails(movie: MovieDetails, isFavorite: Boolean) {
    this.writableDatabase.insert(
        MovieContract.Movie.TABLE_NAME,
        null,
        movie.toContentValues(isFavorite)
    )
  }
  
  fun updateMovieFavoriteStatus(movieId: Long, isFavorite: Boolean) {
    val updateValues = ContentValues().apply {
      put(MovieContract.Movie.COLUMN_IS_FAVORITE, if (isFavorite) 1 else 0)
    }
    
    this.writableDatabase.update(
        MovieContract.Movie.TABLE_NAME,
        updateValues,
        "${MovieContract.Movie.COLUMN_MOVIE_ID} = ?",
        arrayOf(movieId.toString())
    )
  }
}
