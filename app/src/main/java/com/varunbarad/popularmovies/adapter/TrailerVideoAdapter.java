package com.varunbarad.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.model.data.Video;
import com.varunbarad.popularmovies.util.Helper;
import com.varunbarad.popularmovies.util.YouTubeApiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Varun Barad
 * Date: 23-10-2017
 * Project: PopularMovies
 */
public class TrailerVideoAdapter extends RecyclerView.Adapter<TrailerVideoAdapter.ViewHolder> {
  private Context context;
  private List<Video> videos;
  
  public TrailerVideoAdapter(Context context, List<Video> videos) {
    this.context = context;
    this.videos = new ArrayList<>();
    
    for (Video v : videos) {
      if (YouTubeApiHelper.isValidVideoId(v.getKey())) {
        this.videos.add(v);
      }
    }
  }
  
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
    View itemView = LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.list_item_dialog_box_movie_details_video, parent, false);
    
    return new ViewHolder(itemView);
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Picasso
        .with(context)
        .load(YouTubeApiHelper.getDefaultThumbnailUrl(this.videos.get(position).getKey()))
        .error(R.drawable.ic_cloud_off)
        .into(holder.imageViewThumbnail);
    
    holder.textviewTitle.setText(this.videos.get(position).getName());
  }
  
  @Override
  public int getItemCount() {
    if (this.videos != null) {
      return this.videos.size();
    } else {
      return 0;
    }
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    private View itemView;
    
    private AppCompatImageView imageViewThumbnail;
    private AppCompatTextView textviewTitle;
    
    public ViewHolder(View itemView) {
      super(itemView);
      this.itemView = itemView;
      
      this.imageViewThumbnail = this.itemView.findViewById(R.id.imageView_listItemMovieDetailsVideo_thumbnail);
      this.textviewTitle = this.itemView.findViewById(R.id.textView_listItemMovieDetailsVideo_title);
      
      this.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Helper.openYouTubeVideo(
              TrailerVideoAdapter.this.videos.get(getAdapterPosition()).getVideoUrl(),
              TrailerVideoAdapter.this.context
          );
        }
      });
    }
  }
}
