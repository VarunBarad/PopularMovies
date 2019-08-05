package com.varunbarad.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener;
import com.varunbarad.popularmovies.external_services.movie_db_api.MovieDbApiImageHelper;
import com.varunbarad.popularmovies.model.MovieStub;

import java.util.List;

/**
 * Creator: vbarad
 * Date: 2016-12-10
 * Project: PopularMovies
 */

public class TitledMoviesAdapter extends RecyclerView.Adapter<TitledMoviesAdapter.ViewHolder> implements ListItemClickListener {
    private List<MovieStub> movies;
    private ListItemClickListener itemClickListener;

    public TitledMoviesAdapter(List<MovieStub> movies, ListItemClickListener itemClickListener) {
        this.movies = movies;
        this.itemClickListener = itemClickListener;
    }

    public List<MovieStub> getMovies() {
        return this.movies;
    }

    @Override
    public TitledMoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout movieItem = (ConstraintLayout)
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.list_item_recycler_view_movie_details_movies, parent, false);

        TitledMoviesAdapter.ViewHolder viewHolder = new TitledMoviesAdapter.ViewHolder(movieItem, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TitledMoviesAdapter.ViewHolder holder, int position) {
        holder.bind(this.movies.get(position));
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    @Override
    public void onItemClick(int position) {
        this.itemClickListener.onItemClick(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements AppCompatImageView.OnClickListener {
        private AppCompatImageView imageViewMoviePoster;
        private TitledMoviesAdapter adapter;

        public ViewHolder(ConstraintLayout movieItem, TitledMoviesAdapter adapter) {
            super(movieItem);
            this.imageViewMoviePoster = movieItem.findViewById(R.id.imageView_listItemMovieDetailsMovies_poster);
            this.adapter = adapter;
            this.imageViewMoviePoster.setOnClickListener(this);
        }

        private void bind(MovieStub movie) {
            int imageWidth = this.imageViewMoviePoster.getWidth();

            String imageUrl = MovieDbApiImageHelper.getImageUrl(
                    movie.getPosterPath(),
                    imageWidth
            );

            Picasso.with(this.imageViewMoviePoster.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_poster)
                    .error(R.drawable.ic_cloud_off)
                    .into(this.imageViewMoviePoster);

            this.imageViewMoviePoster.setContentDescription(movie.getTitle());
        }

        @Override
        public void onClick(View view) {
            this.adapter.onItemClick(this.getAdapterPosition());
        }
    }
}
