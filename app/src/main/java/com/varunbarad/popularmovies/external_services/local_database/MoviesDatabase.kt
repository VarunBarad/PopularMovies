package com.varunbarad.popularmovies.external_services.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.varunbarad.popularmovies.external_services.local_database.movie_details.MovieDetailsDao
import com.varunbarad.popularmovies.external_services.local_database.movie_details.MovieDetailsDb

@Database(
    entities = [
        MovieDetailsDb::class
    ],
    version = MoviesDatabase.databaseVersion
)
@TypeConverters(RoomTypeConverters::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDetailsDao(): MovieDetailsDao

    companion object {
        const val databaseVersion = 1
        const val databaseName = "PopularMoviesDatabase"
    }
}
