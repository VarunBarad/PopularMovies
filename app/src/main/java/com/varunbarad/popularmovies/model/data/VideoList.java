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
  @SerializedName("results")
  private ArrayList<Video> results;

  /**
   * Public no-args constructor for serialization
   */
  public VideoList() {

  }

  /**
   * @param results
   */
  public VideoList(final ArrayList<Video> results) {
    this.results = results;
  }
  
  public ArrayList<Video> getResults() {
    return results;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
