package com.varunbarad.popularmovies.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Creator: vbarad
 * Date: 2016-12-04
 * Project: PopularMovies
 */
public final class Helper {
  public static void openUrlInBrowser(String url, Context context) {
    Uri websiteUri = Uri.parse(url);
    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, websiteUri);
    if (websiteIntent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(websiteIntent);
    }
  }
  
  public static void openYouTubeVideo(String videoUrl, Context context) {
    Uri videoUri = Uri.parse(videoUrl);
    Intent videoIntent = new Intent(Intent.ACTION_VIEW, videoUri);
    if (videoIntent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(videoIntent);
    }
  }
}
