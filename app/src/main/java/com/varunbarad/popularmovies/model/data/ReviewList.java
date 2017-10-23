package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-12-08
 * Project: PopularMovies
 */

public final class ReviewList {
  @Expose
  @SerializedName("page")
  private long page;
  @Expose
  @SerializedName("results")
  private ArrayList<Review> results;
  @Expose
  @SerializedName("total_pages")
  private long totalPages;
  @Expose
  @SerializedName("total_results")
  private long totalResults;

  /**
   * Public no-args constructor for serialization
   */
  public ReviewList() {

  }

  /**
   * @param page
   * @param results
   * @param totalPages
   * @param totalResults
   */
  public ReviewList(final long page, final ArrayList<Review> results, final long totalPages, final long totalResults) {
    this.page = page;
    this.results = results;
    this.totalPages = totalPages;
    this.totalResults = totalResults;
  }

  public long getPage() {
    return page;
  }
  
  public ArrayList<Review> getResults() {
    return results;
  }
  
  public void setResults(ArrayList<Review> results) {
    this.results = results;
  }

  public long getTotalPages() {
    return totalPages;
  }

  public long getTotalResults() {
    return totalResults;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
