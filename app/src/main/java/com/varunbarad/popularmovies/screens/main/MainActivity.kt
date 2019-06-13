package com.varunbarad.popularmovies.screens.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.varunbarad.popularmovies.R
import com.varunbarad.popularmovies.databinding.ActivityMainBinding
import com.varunbarad.popularmovies.di.external_services.LocalDatabaseModule
import com.varunbarad.popularmovies.eventlistener.FragmentInteractionEvent
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener
import com.varunbarad.popularmovies.external_services.local_database.movie_details.MovieDetailsDao
import com.varunbarad.popularmovies.model.data.MovieStub
import com.varunbarad.popularmovies.screens.main.movie_details.MovieDetailsFragment
import com.varunbarad.popularmovies.screens.main.movies_list.MoviesListFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    private val disposable: CompositeDisposable = CompositeDisposable()

    private val moviesDao: MovieDetailsDao by lazy {
        LocalDatabaseModule(this.application).provideMoviesDao()
    }

  private lateinit var dataBinding: ActivityMainBinding

  private var isDualPane: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    this.isDualPane = (this.dataBinding.fragmentDetailsContainer?.visibility == View.VISIBLE)

    this.setSupportActionBar(this.dataBinding.toolbar)
    this.supportActionBar?.setTitle(R.string.app_name)

    if (savedInstanceState == null) {
      val moviesListFragment: MoviesListFragment = MoviesListFragment.newInstance()

      this.supportFragmentManager
          .beginTransaction()
          .add(R.id.fragment_main_container, moviesListFragment)
          .commit()
    }

    if (this.isDualPane) {
      val movieDetailsFragment = this.supportFragmentManager.findFragmentById(R.id.fragment_main_container)
      if (movieDetailsFragment is MovieDetailsFragment) {
        val movieId = movieDetailsFragment.getMovieId()
        while (this.supportFragmentManager.findFragmentById(R.id.fragment_main_container) is MovieDetailsFragment) {
          this.onBackPressed()
        }

          this.disposable.add(
              Single.fromCallable { this.moviesDao.getMovieDetails(movieId) }
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeBy(
                      onError = { Log.w(this.packageName, it.message) },
                      onSuccess = {
                          if (it != null) {
                              this.showDetails(it.toMovieDetails().toMovieStub())
                          }
                      }
                  )
          )
      }
    } else {
      val movieDetailsFragment = this.supportFragmentManager.findFragmentById(R.id.fragment_details_container)
      if (movieDetailsFragment is MovieDetailsFragment) {
        val movieId = movieDetailsFragment.getMovieId()
        movieDetailsFragment.onStop()

        this.supportFragmentManager
            .beginTransaction()
            .remove(movieDetailsFragment)
            .commit()

          this.disposable.add(
              Single.fromCallable { this.moviesDao.getMovieDetails(movieId) }
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeBy(
                      onError = { Log.w(this.packageName, it.message) },
                      onSuccess = {
                          if (it != null) {
                              this.showDetails(it.toMovieDetails().toMovieStub())
                          }
                      }
                  )
          )
      }
    }
  }

  override fun onFragmentInteraction(event: FragmentInteractionEvent) {
    when (event) {
      is FragmentInteractionEvent.OpenMovieDetailsEvent -> {
        this.showDetails(event.movie)
      }
      is FragmentInteractionEvent.AddToFavoriteEvent -> {
        val moviesListFragment = this.supportFragmentManager.findFragmentById(R.id.fragment_main_container)
        if (moviesListFragment is MoviesListFragment) {
          moviesListFragment.addFavorite(event.movie)
        }
      }
      is FragmentInteractionEvent.RemoveFromFavoriteEvent -> {
        val moviesListFragment = this.supportFragmentManager.findFragmentById(R.id.fragment_main_container)
        if (moviesListFragment is MoviesListFragment) {
          moviesListFragment.removeFavorite(event.movieId)
        }
      }
    }
  }

  private fun showDetails(movie: MovieStub) {
    val movieDetailsFragment = MovieDetailsFragment.newInstance(movie)

    if (this.isDualPane) {
      this.supportFragmentManager
          .beginTransaction()
          .replace(R.id.fragment_details_container, movieDetailsFragment)
          .commit()
    } else {
      this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

      this.supportFragmentManager
          .beginTransaction()
          .replace(R.id.fragment_main_container, movieDetailsFragment)
          .addToBackStack(null)
          .commit()
    }
  }

  fun checkDualPane(): Boolean = this.isDualPane

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        this.onBackPressed()
        true
      }
      else -> {
        super.onOptionsItemSelected(item)
      }
    }
  }

  override fun onDestroy() {
      this.disposable.clear()
    super.onDestroy()
  }

  companion object {
    @JvmStatic
    fun launch(context: Context) {
      context.startActivity(Intent(context, MainActivity::class.java))
    }
  }
}
