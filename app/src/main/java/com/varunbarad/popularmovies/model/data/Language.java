package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Creator: vbarad
 * Date: 2016-12-07
 * Project: PopularMovies
 */

public final class Language {
  @Expose
  @SerializedName("iso_639_1")
  private String isoCode;
  @Expose
  @SerializedName("name")
  private String name;

  /**
   * Public no-arg constructor for serialization
   */
  public Language() {

  }

  /**
   * @param isoCode
   * @param name
   */
  public Language(String isoCode, String name) {
    this.isoCode = isoCode;
    this.name = name;
  }

  public String getIsoCode() {
    return isoCode;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
