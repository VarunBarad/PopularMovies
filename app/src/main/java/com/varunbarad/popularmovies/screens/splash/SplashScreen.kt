package com.varunbarad.popularmovies.screens.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.varunbarad.popularmovies.activity.MainActivity

/**
 * Creator: Varun Barad
 * Date: 2019-06-04
 * Project: PopularMovies
 */
class SplashScreen : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MainActivity.startActivity(this)
    this.finish()
  }
}
