package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Creator: vbarad
 * Date: 2016-12-07
 * Project: PopularMovies
 */

public final class Country {
  @Expose
  @SerializedName("iso_3166_1")
  private String isoName;
  @Expose
  @SerializedName("name")
  private String name;

  /**
   * Public no-args constructor for serialization
   */
  public Country() {

  }

  /**
   * @param isoName
   * @param name
   */
  public Country(final String isoName, final String name) {
    this.isoName = isoName;
    this.name = name;
  }

  public String getIsoName() {
    return isoName;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
