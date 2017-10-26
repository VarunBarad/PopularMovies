package com.varunbarad.popularmovies.eventlistener;

/**
 * Creator: vbarad
 * Date: 2016-12-04
 * Project: PopularMovies
 */

public interface OnFragmentInteractionListener {
  String TAG_MOVIE = "MOVIE";
  String TAG_FAVORITE = "FAVORITE";
  String TAG_UNFAVORITE = "UNFAVORITE";
  
  void onFragmentInteraction(String message);
}
