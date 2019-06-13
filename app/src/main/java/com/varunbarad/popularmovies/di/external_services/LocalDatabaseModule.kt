package com.varunbarad.popularmovies.di.external_services

import android.app.Application
import androidx.room.Room
import com.varunbarad.popularmovies.external_services.local_database.MoviesDatabase
import com.varunbarad.popularmovies.external_services.local_database.movie_details.MovieDetailsDao

class LocalDatabaseModule(application: Application) {
    private val moviesDatabase: MoviesDatabase = Room
        .databaseBuilder(
            application,
            MoviesDatabase::class.java,
            MoviesDatabase.databaseName
        ).fallbackToDestructiveMigration()
        .build()

    fun provideMoviesDao(): MovieDetailsDao {
        return this.moviesDatabase.movieDetailsDao()
    }
}
