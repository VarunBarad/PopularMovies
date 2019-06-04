package com.varunbarad.popularmovies.screens.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.varunbarad.popularmovies.screens.main.MainActivity

/**
 * Creator: Varun Barad
 * Date: 2019-06-04
 * Project: PopularMovies
 */
class SplashScreen : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MainActivity.launch(this)
    this.finish()
  }
}
