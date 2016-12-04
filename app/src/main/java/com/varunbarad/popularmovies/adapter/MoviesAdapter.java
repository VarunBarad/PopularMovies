package com.varunbarad.popularmovies.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener;
import com.varunbarad.popularmovies.model.data.MovieStub;
import com.varunbarad.popularmovies.util.Helper;
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiImageHelper;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-12-04
 * Project: PopularMovies
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> implements ListItemClickListener {
  private ArrayList<MovieStub> movies;
  private ListItemClickListener itemClickListener;

  public MoviesAdapter(ArrayList<MovieStub> movies, ListItemClickListener itemClickListener) {
    this.movies = movies;
    this.itemClickListener = itemClickListener;
  }

  public ArrayList<MovieStub> getMovies() {
    return this.movies;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    AppCompatImageView movieImage = (AppCompatImageView)
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.list_item_recycler_view_movies, parent, false);
    ViewHolder viewHolder = new ViewHolder(movieImage, this);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    int imageWidth = Helper.getScreenWidth(holder.imageItem) / 2;

    String imageUrl = MovieDbApiImageHelper.getImageUrl(
        this.movies.get(position).getPosterPath(),
        imageWidth
    );

    Picasso
        .with(holder.imageItem.getContext())
        .load(imageUrl)
        .placeholder(R.drawable.placeholder_poster)
        .error(R.drawable.ic_cloud_off_black)
        .into(holder.imageItem);
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
    private AppCompatImageView imageItem;
    private MoviesAdapter adapter;

    public ViewHolder(AppCompatImageView imageItem, MoviesAdapter adapter) {
      super(imageItem);
      this.imageItem = imageItem;
      this.adapter = adapter;
      this.imageItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      this.adapter.onItemClick(this.getAdapterPosition());
    }
  }
}
