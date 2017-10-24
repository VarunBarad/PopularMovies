package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Creator: vbarad
 * Date: 2016-12-08
 * Project: PopularMovies
 */

public final class Image {
  @Expose
  @SerializedName("aspect_ratio")
  private double aspectRatio;
  @Expose
  @SerializedName("file_path")
  private String imagePath;
  @Expose
  @SerializedName("width")
  private int width;
  @Expose
  @SerializedName("height")
  private int height;
  @Expose
  @SerializedName("iso_639_1")
  private String isoLanguageCode;
  @Expose
  @SerializedName("vote_average")
  private double averageVotes;
  @Expose
  @SerializedName("vote_count")
  private long numberOfVotes;

  /**
   * Public no-args constructor for serialization
   */
  public Image() {

  }

  /**
   * @param aspectRatio
   * @param imagePath
   * @param width
   * @param height
   * @param isoLanguageCode
   * @param averageVotes
   * @param numberOfVotes
   */
  public Image(final double aspectRatio, final String imagePath, final int width, final int height, final String isoLanguageCode, final double averageVotes, final long numberOfVotes) {
    this.aspectRatio = aspectRatio;
    this.imagePath = imagePath;
    this.width = width;
    this.height = height;
    this.isoLanguageCode = isoLanguageCode;
    this.averageVotes = averageVotes;
    this.numberOfVotes = numberOfVotes;
  }

  public double getAspectRatio() {
    return aspectRatio;
  }

  public String getImagePath() {
    return imagePath;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
  
  public String getIsoLanguageCode() {
    return isoLanguageCode;
  }

  public double getAverageVotes() {
    return averageVotes;
  }

  public long getNumberOfVotes() {
    return numberOfVotes;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
