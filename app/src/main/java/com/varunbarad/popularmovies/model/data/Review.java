package com.varunbarad.popularmovies.model.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Creator: vbarad
 * Date: 2016-12-08
 * Project: PopularMovies
 */

public final class Review {
  private static final int SHORTENED_LENGTH = 200;
  private static final String ellipsis = "...";
  
  @Expose
  @SerializedName("id")
  private String id;
  @Expose
  @SerializedName("author")
  private String author;
  @Expose
  @SerializedName("content")
  private String content;
  @Expose
  @SerializedName("url")
  private String url;

  /**
   * Public no-args constructor for serialization
   */
  public Review() {

  }

  /**
   * @param id
   * @param author
   * @param content
   * @param url
   */
  public Review(final String id, final String author, final String content, final String url) {
    this.id = id;
    this.author = author;
    this.content = content;
    this.url = url;
  }

  public final String getId() {
    return id;
  }

  public final String getAuthor() {
    return author;
  }

  public final String getContent() {
    return content;
  }

  public final String getUrl() {
    return url;
  }

  @Override
  public String toString() {
    return (new Gson()).toJson(this);
  }
  
  public final String getShortenedContent() {
    String content = this.getContent();
    if (content.length() > Review.SHORTENED_LENGTH) {
      return content.substring(0, Review.SHORTENED_LENGTH - Review.ellipsis.length()) + Review.ellipsis;
    } else {
      return content;
    }
  }
}
