package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-12-07
 * Project: PopularMovies
 */

public final class VideoList {
  @Expose
  @SerializedName("videos")
  private ArrayList<Video> videos;

  /**
   * Public no-args constructor for serialization
   */
  public VideoList() {

  }

  /**
   * @param videos
   */
  public VideoList(final ArrayList<Video> videos) {
    this.videos = videos;
  }

  public ArrayList<Video> getVideos() {
    return videos;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
