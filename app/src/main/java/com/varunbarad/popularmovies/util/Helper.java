package com.varunbarad.popularmovies.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Creator: vbarad
 * Date: 2016-12-04
 * Project: PopularMovies
 */

public final class Helper {
  public static int getScreenWidth(View view) {
    int screenSize = view.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    int width;
    if (view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          width = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          width = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          width = 480;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          width = 720;
          break;
        default:
          width = 480;
          break;
      }
    } else {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          width = 426;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          width = 470;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          width = 640;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          width = 960;
          break;
        default:
          width = 640;
          break;
      }
    }
    return width;
  }
  
  public static int getScreenHeight(View view) {
    int screenSize = view.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    int height;
    if (view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          height = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          height = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          height = 480;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          height = 720;
          break;
        default:
          height = 480;
          break;
      }
    } else {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          height = 426;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          height = 470;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          height = 640;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          height = 960;
          break;
        default:
          height = 640;
          break;
      }
    }
    return height;
  }
  
  public static boolean isConnectedToInternet(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    
    boolean isConnected;
    
    isConnected = (activeNetwork != null) && activeNetwork.isConnected();
    
    return isConnected;
  }
  
  public static int convertDpToPx(Context context, int dp) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    int px = Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return px;
  }
  
  public static int convertPxToDp(Context context, int px) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    int dp = Math.round(px / (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return dp;
  }
  
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
