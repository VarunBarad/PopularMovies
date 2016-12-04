package com.varunbarad.popularmovies.util.MovieDbApi;

/**
 * Creator: vbarad
 * Date: 2016-12-04
 * Project: PopularMovies
 */

public final class MovieDbApiImageHelper {
  private static final String baseUrlImage = "https://image.tmdb.org/t/p/";

  /**
   * Get the URL(for specific width) for image given it's ID
   *
   * @param imageId ID of the image whose URL is needed
   * @param widthPx The width(in px) of the view which is to be used to display the image
   * @return The URL of the image(of appropriate width) whose ID was passed
   */
  public static String getImageUrl(String imageId, int widthPx) {
    String imageSize;
    if (widthPx > 780) {
      imageSize = "original";
    } else if (widthPx > 500 && widthPx <= 780) {
      imageSize = "w780";
    } else if (widthPx > 342 && widthPx <= 500) {
      imageSize = "w500";
    } else if (widthPx > 185 && widthPx <= 342) {
      imageSize = "w342";
    } else if (widthPx > 154 && widthPx <= 185) {
      imageSize = "w185";
    } else if (widthPx > 92 && widthPx <= 154) {
      imageSize = "w154";
    } else if (widthPx > 0 && widthPx <= 92) {
      imageSize = "w92";
    } else {
      imageSize = "w185";     // Default Value
    }

    return baseUrlImage + imageSize + "/" + imageId;
  }

  /**
   * Get the URL(for original size) for image given it's ID
   *
   * @param imageId ID of the image whose URL is needed
   * @return The URL of the image(of appropriate width) whose ID was passed
   */
  public static String getOriginalImageUrl(String imageId) {
    return baseUrlImage + "original/" + imageId;
  }
}
