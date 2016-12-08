package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Creator: vbarad
 * Date: 2016-12-07
 * Project: PopularMovies
 */

public final class CollectionStub {
  @Expose
  @SerializedName("id")
  private long id;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("poster_path")
  private String posterPath;
  @Expose
  @SerializedName("backdrop_path")
  private String backdropPath;

  /**
   * Public no-args constructor for serialization
   */
  public CollectionStub() {

  }

  /**
   * @param id
   * @param name
   * @param posterPath
   * @param backdropPath
   */
  public CollectionStub(final long id, final String name, final String posterPath, final String backdropPath) {
    this.id = id;
    this.name = name;
    this.posterPath = posterPath;
    this.backdropPath = backdropPath;
  }

  public final long getId() {
    return id;
  }

  public final String getName() {
    return name;
  }

  public final String getPosterPath() {
    return posterPath;
  }

  public final String getBackdropPath() {
    return backdropPath;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
