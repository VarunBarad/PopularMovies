package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Creator: vbarad
 * Date: 2016-11-24
 * Project: PopularMovies
 */

public class MovieList {
  @Expose
  @SerializedName("page")
  private long page;
  @Expose
  @SerializedName("results")
  private ArrayList<MovieStub> results;
  @Expose
  @SerializedName("total_results")
  private long totalResults;
  @Expose
  @SerializedName("total_pages")
  private long totalPages;

  /**
   * No args contryctor for use in serialization
   */
  public MovieList() {
  }

  /**
   * @param page
   * @param results
   * @param totalResults
   * @param totalPages
   */
  public MovieList(long page, ArrayList<MovieStub> results, long totalResults, long totalPages) {
    this.page = page;
    this.results = results;
    this.totalResults = totalResults;
    this.totalPages = totalPages;
  }

  public long getPage() {
    return page;
  }

  public ArrayList<MovieStub> getResults() {
    return results;
  }

  public void setResults(ArrayList<MovieStub> results) {
    this.results = results;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public long getTotalPages() {
    return totalPages;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
