package com.varunbarad.popularmovies.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener;
import com.varunbarad.popularmovies.model.data.Genre;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-12-10
 * Project: PopularMovies
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> implements ListItemClickListener {
  private ArrayList<Genre> genres;
  private ListItemClickListener itemClickListener;
  
  public GenreAdapter(ArrayList<Genre> genres, ListItemClickListener itemClickListener) {
    this.genres = genres;
    this.itemClickListener = itemClickListener;
  }
  
  public ArrayList<Genre> getGenres() {
    return this.genres;
  }
  
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    AppCompatTextView textViewGenre = (AppCompatTextView)
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.list_item_recycler_view_movie_details_genres, parent, false);
    ViewHolder viewHolder = new ViewHolder(textViewGenre, this);
    return viewHolder;
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder
        .genreItem
        .setText(
            this.genres.get(position).getName()
        );
  }
  
  @Override
  public int getItemCount() {
    return this.genres.size();
  }
  
  @Override
  public void onItemClick(int position) {
    this.itemClickListener.onItemClick(position);
  }
  
  static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private GenreAdapter adapter;
    private AppCompatTextView genreItem;
    
    public ViewHolder(AppCompatTextView genreItem, GenreAdapter adapter) {
      super(genreItem);
      
      this.genreItem = genreItem;
      this.adapter = adapter;
      
      this.genreItem.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
      this.adapter.onItemClick(this.getAdapterPosition());
    }
  }
}
