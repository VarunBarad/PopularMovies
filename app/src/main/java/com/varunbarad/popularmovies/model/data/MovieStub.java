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

public class MovieStub {
  @SerializedName("poster_path")
  @Expose
  private String posterPath;
  @SerializedName("adult")
  @Expose
  private boolean adult;
  @SerializedName("overview")
  @Expose
  private String overview;
  @SerializedName("release_date")
  @Expose
  private String releaseDate;
  @SerializedName("genre_ids")
  @Expose
  private ArrayList<Long> genreIds;
  @SerializedName("id")
  @Expose
  private long id;
  @SerializedName("original_title")
  @Expose
  private String originalTitle;
  @SerializedName("original_language")
  @Expose
  private String originalLanguage;
  @SerializedName("title")
  @Expose
  private String title;
  @SerializedName("backdrop_path")
  @Expose
  private String backdropPath;
  @SerializedName("popularity")
  @Expose
  private double popularity;
  @SerializedName("vote_count")
  @Expose
  private long voteCount;
  @SerializedName("video")
  @Expose
  private boolean video;
  @SerializedName("vote_average")
  @Expose
  private double voteAverage;

  /**
   * No args constructor for use in serialization
   */
  public MovieStub() {
  }

  /**
   * @param id
   * @param genreIds
   * @param title
   * @param releaseDate
   * @param overview
   * @param posterPath
   * @param originalTitle
   * @param voteAverage
   * @param originalLanguage
   * @param adult
   * @param backdropPath
   * @param voteCount
   * @param video
   * @param popularity
   */
  public MovieStub(String posterPath, boolean adult, String overview, String releaseDate, ArrayList<Long> genreIds, long id, String originalTitle, String originalLanguage, String title, String backdropPath, double popularity, long voteCount, boolean video, double voteAverage) {
    this.posterPath = posterPath;
    this.adult = adult;
    this.overview = overview;
    this.releaseDate = releaseDate;
    this.genreIds = genreIds;
    this.id = id;
    this.originalTitle = originalTitle;
    this.originalLanguage = originalLanguage;
    this.title = title;
    this.backdropPath = backdropPath;
    this.popularity = popularity;
    this.voteCount = voteCount;
    this.video = video;
    this.voteAverage = voteAverage;
  }

  /**
   * @return The image path for poster
   */
  public String getPosterPath() {
    return posterPath;
  }

  /**
   * @return `true` if the movie is an adult movie
   */
  public boolean isAdult() {
    return adult;
  }

  /**
   * @return The overview
   */
  public String getOverview() {
    return overview;
  }

  /**
   * @return The releaseDate
   */
  public String getReleaseDate() {
    return releaseDate;
  }

  /**
   * @return The genreIds
   */
  public ArrayList<Long> getGenreIds() {
    return genreIds;
  }

  /**
   * @return The id
   */
  public long getId() {
    return id;
  }

  /**
   * @return The originalTitle
   */
  public String getOriginalTitle() {
    return originalTitle;
  }

  /**
   * @return The originalLanguage
   */
  public String getOriginalLanguage() {
    return originalLanguage;
  }

  /**
   * @return The title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return The backdropPath
   */
  public String getBackdropPath() {
    return backdropPath;
  }

  /**
   * @return The popularity
   */
  public double getPopularity() {
    return popularity;
  }

  /**
   * @return The voteCount
   */
  public long getVoteCount() {
    return voteCount;
  }

  /**
   * @return The video
   */
  public boolean isVideo() {
    return video;
  }

  /**
   * @return The voteAverage
   */
  public double getVoteAverage() {
    return voteAverage;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
}
