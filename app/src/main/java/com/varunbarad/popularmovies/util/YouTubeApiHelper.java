package com.varunbarad.popularmovies.util;

import java.util.Locale;

/**
 * Creator: Varun Barad
 * Date: 23-10-2017
 * Project: PopularMovies
 */
public final class YouTubeApiHelper {
  private static final String THUMBNAIL_IMAGE_FORMAT = "https://i.ytimg.com/vi/%s/default.jpg";
  
  public static boolean isValidVideoId(String videoId) {
    return videoId.trim().matches("[0-9a-zA-Z-_]{11}");
  }
  
  public static String getDefaultThumbnailUrl(String videoId) {
    if (YouTubeApiHelper.isValidVideoId(videoId)) {
      return String.format(Locale.ENGLISH, THUMBNAIL_IMAGE_FORMAT, videoId);
    } else {
      return null;
    }
  }
}
