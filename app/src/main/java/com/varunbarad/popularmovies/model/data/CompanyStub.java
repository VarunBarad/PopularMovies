package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Creator: vbarad
 * Date: 2016-12-07
 * Project: PopularMovies
 */

public final class CompanyStub {
  @Expose
  @SerializedName("id")
  private long id;
  @Expose
  @SerializedName("name")
  private String name;

  /**
   * No args constructor for serialization
   */
  public CompanyStub() {

  }

  /**
   * @param id
   * @param name
   */
  public CompanyStub(final long id, final String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * @return The unique id for this Genre
   */
  public final long getId() {
    return id;
  }

  /**
   * @return The name for this Genre
   */
  public final String getName() {
    return name;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
