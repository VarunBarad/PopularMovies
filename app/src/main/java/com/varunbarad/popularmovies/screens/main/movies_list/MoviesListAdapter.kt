package com.varunbarad.popularmovies.screens.main.movies_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.varunbarad.popularmovies.R
import com.varunbarad.popularmovies.model.MovieStub

class MoviesListAdapter(
    initialMovies: List<MovieStub>,
    private val onSelectMovieListener: (MovieStub) -> Unit
) : RecyclerView.Adapter<MovieViewHolder>() {
    private val movies: MutableList<MovieStub> = initialMovies.toMutableList()

    fun addMovie(movie: MovieStub) {
        this.movies.add(movie)
        this.notifyItemInserted(this.itemCount - 1)
    }

    fun removeMovie(movieId: Long) {
        val position = this.movies.indexOfFirst { it.id == movieId }

        if (position != -1) {
            this.movies.removeAt(position)
            this.notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_recycler_view_movies,
                parent,
                false
            ) as AppCompatImageView,
            this.onSelectMovieListener
        )
    }

    override fun getItemCount(): Int = this.movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(this.movies[position])
    }
}
