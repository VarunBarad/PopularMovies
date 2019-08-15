package com.varunbarad.popularmovies.screens.main.movies_list

import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.varunbarad.popularmovies.R
import com.varunbarad.popularmovies.external_services.movie_db_api.getImageUrl
import com.varunbarad.popularmovies.model.MovieStub

class MovieViewHolder(
    rootView: AppCompatImageView,
    private val movieClickListener: (MovieStub) -> Unit
) : RecyclerView.ViewHolder(rootView) {
    private val moviePosterImageView: AppCompatImageView = rootView

    fun bind(movie: MovieStub) {
        val imageUrl = getImageUrl(movie.posterPath, this.moviePosterImageView.width)

        Picasso
            .with(this.moviePosterImageView.context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_poster)
            .error(R.drawable.ic_cloud_off)
            .into(this.moviePosterImageView)

        this.moviePosterImageView.setOnClickListener { this.movieClickListener(movie) }
    }
}
