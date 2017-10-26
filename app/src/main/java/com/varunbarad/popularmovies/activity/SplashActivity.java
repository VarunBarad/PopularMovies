package com.varunbarad.popularmovies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Creator: Varun Barad
 * Date: 27-10-2017
 * Project: PopularMovies
 */
public class SplashActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MainActivity.startActivity(this);
    this.finish();
  }
}
