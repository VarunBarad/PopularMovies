package com.varunbarad.popularmovies.external_services.local_database.movie_details

import androidx.room.*

@Dao
interface MovieDetailsDao {
    @Query("select * from movie where is_favorite = 1")
    fun getFavoriteMovies(): List<MovieDetailsDb>

    @Query("select * from movie where movie_id = :id")
    fun getMovieDetails(id: Long): MovieDetailsDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieDetailsDb)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavoriteStatus(movie: MovieDetailsDb)
}
