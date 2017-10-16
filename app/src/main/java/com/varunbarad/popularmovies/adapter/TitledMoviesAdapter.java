package com.varunbarad.popularmovies.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener;
import com.varunbarad.popularmovies.model.data.MovieStub;
import com.varunbarad.popularmovies.util.Helper;
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiImageHelper;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-12-10
 * Project: PopularMovies
 */

public class TitledMoviesAdapter extends RecyclerView.Adapter<TitledMoviesAdapter.ViewHolder> implements ListItemClickListener {
  private ArrayList<MovieStub> movies;
  private ListItemClickListener itemClickListener;
  
  public TitledMoviesAdapter(ArrayList<MovieStub> movies, ListItemClickListener itemClickListener) {
    this.movies = movies;
    this.itemClickListener = itemClickListener;
  }
  
  public ArrayList<MovieStub> getMovies() {
    return this.movies;
  }
  
  @Override
  public TitledMoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RelativeLayout movieItem = (RelativeLayout)
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.list_item_recycler_view_movie_details_movies, parent, false);
    
    AppCompatImageView movieImage = (AppCompatImageView) movieItem.findViewById(R.id.imageView_listItemMovieDetailsMovies_poster);
    AppCompatTextView movieTitle = (AppCompatTextView) movieItem.findViewById(R.id.textView_listItemMovieDetailsMovies_title);
    
    TitledMoviesAdapter.ViewHolder viewHolder = new TitledMoviesAdapter.ViewHolder(movieImage, movieTitle, this);
    return viewHolder;
  }
  
  @Override
  public void onBindViewHolder(TitledMoviesAdapter.ViewHolder holder, int position) {
    int imageWidth = Helper.convertDpToPx(
        holder.imageViewMoviePoster.getContext(),
        holder.imageViewMoviePoster.getWidth()
    );
    
    String imageUrl = MovieDbApiImageHelper.getImageUrl(
        this.movies.get(position).getPosterPath(),
        imageWidth
    );
    
    Picasso
        .with(holder.imageViewMoviePoster.getContext())
        .load(imageUrl)
        .placeholder(R.drawable.placeholder_poster)
        .error(R.drawable.ic_cloud_off_black)
        .into(holder.imageViewMoviePoster);
    
    holder
        .textViewMovieTitle
        .setText(this.movies.get(position).getTitle());
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
    private AppCompatTextView textViewMovieTitle;
    private TitledMoviesAdapter adapter;
    
    public ViewHolder(AppCompatImageView imageViewMoviePoster, AppCompatTextView textViewMovieTitle, TitledMoviesAdapter adapter) {
      super(imageViewMoviePoster);
      this.imageViewMoviePoster = imageViewMoviePoster;
      this.textViewMovieTitle = textViewMovieTitle;
      this.adapter = adapter;
      this.imageViewMoviePoster.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
      this.adapter.onItemClick(this.getAdapterPosition());
    }
  }
}
