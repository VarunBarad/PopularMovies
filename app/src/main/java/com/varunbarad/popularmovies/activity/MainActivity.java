package com.varunbarad.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.databinding.ActivityMainBinding;
import com.varunbarad.popularmovies.eventlistener.FragmentInteractionEvent;
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener;
import com.varunbarad.popularmovies.fragment.MovieDetailsFragment;
import com.varunbarad.popularmovies.fragment.MoviesListFragment;
import com.varunbarad.popularmovies.model.data.MovieDetails;
import com.varunbarad.popularmovies.model.data.MovieStub;
import com.varunbarad.popularmovies.util.Helper;
import com.varunbarad.popularmovies.util.data.MovieContract;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
  
  private ActivityMainBinding dataBinding;
  
  private boolean isDualPane;
  
  public static void startActivity(Context context) {
    Intent mainActivityIntent = new Intent(context, MainActivity.class);
    context.startActivity(mainActivityIntent);
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
  
    {
      View v = findViewById(R.id.fragment_details_container);
      this.isDualPane = v != null && v.getVisibility() == View.VISIBLE;
    }
  
    this.setSupportActionBar(this.dataBinding.toolbar);
    if (this.getSupportActionBar() != null) {
      this.getSupportActionBar().setTitle(R.string.app_name);
    }
  
    if (savedInstanceState == null) {
      MoviesListFragment moviesListFragment = MoviesListFragment.newInstance();
    
      FragmentManager fragmentManager = this.getSupportFragmentManager();
      fragmentManager
          .beginTransaction()
          .add(R.id.fragment_main_container, moviesListFragment)
          .commit();
    }
  
    if (isDualPane) {
      if (this.getSupportFragmentManager().findFragmentById(R.id.fragment_main_container) instanceof MovieDetailsFragment) {
        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_main_container);
        long movieId = movieDetailsFragment.getMovieId();
        while (this.getSupportFragmentManager().findFragmentById(R.id.fragment_main_container) instanceof MovieDetailsFragment) {
          this.onBackPressed();
        }
      
        Cursor cursor = this.getContentResolver().query(
            MovieContract.Movie.buildUriWithMovieId(movieId),
            null,
            null,
            null,
            null
        );
      
        if (cursor != null) {
          if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            this.showDetails(Helper.movieStubFromMovieDetails(Helper.readOneMovie(cursor)));
          }
        
          cursor.close();
        }
      }
    } else {
      if (this.getSupportFragmentManager().findFragmentById(R.id.fragment_details_container) != null) {
        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_details_container);
        long movieId = movieDetailsFragment.getMovieId();
        movieDetailsFragment.onStop();
      
        this.getSupportFragmentManager()
            .beginTransaction()
            .remove(movieDetailsFragment)
            .commit();
      
        Cursor cursor = this.getContentResolver().query(
            MovieContract.Movie.buildUriWithMovieId(movieId),
            null,
            null,
            null,
            null
        );
      
        if (cursor != null) {
          if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            this.showDetails(Helper.movieStubFromMovieDetails(Helper.readOneMovie(cursor)));
          }
        
          cursor.close();
        }
      }
    }
  }

  @Override
  public void onFragmentInteraction(@NotNull FragmentInteractionEvent event) {
    if (event instanceof FragmentInteractionEvent.OpenMovieDetailsEvent) {
      FragmentInteractionEvent.OpenMovieDetailsEvent movieDetailsEvent = (FragmentInteractionEvent.OpenMovieDetailsEvent) event;
      this.showDetails(movieDetailsEvent.getMovie());
    } else if (event instanceof FragmentInteractionEvent.AddToFavoriteEvent) {
      FragmentInteractionEvent.AddToFavoriteEvent addToFavoriteEvent = (FragmentInteractionEvent.AddToFavoriteEvent) event;
      MovieStub movie = addToFavoriteEvent.getMovie();
      if (this.getSupportFragmentManager().findFragmentById(R.id.fragment_main_container) instanceof MoviesListFragment) {
        MoviesListFragment moviesListFragment = (MoviesListFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_main_container);
        moviesListFragment.addFavorite(movie);
      }
    } else if (event instanceof FragmentInteractionEvent.RemoveFromFavoriteEvent) {
      FragmentInteractionEvent.RemoveFromFavoriteEvent removeFromFavoriteEvent = (FragmentInteractionEvent.RemoveFromFavoriteEvent) event;
      long movieId = removeFromFavoriteEvent.getMovieId();
      if (this.getSupportFragmentManager().findFragmentById(R.id.fragment_main_container) instanceof MoviesListFragment) {
        MoviesListFragment moviesListFragment = (MoviesListFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_main_container);
        moviesListFragment.removeFavorite(movieId);
      }
    }
  }
  
  private void showDetails(MovieStub movie) {
    MovieDetailsFragment detailsFragment = MovieDetailsFragment.newInstance(movie);
    FragmentManager fragmentManager = this.getSupportFragmentManager();
    
    if (this.isDualPane) {
      fragmentManager
          .beginTransaction()
          .replace(R.id.fragment_details_container, detailsFragment)
          .commit();
    } else {
      if (this.getSupportActionBar() != null) {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      }
  
      fragmentManager
          .beginTransaction()
          .replace(R.id.fragment_main_container, detailsFragment)
          .addToBackStack(null)
          .commit();
    }
  }
  
  public boolean checkDualPane() {
    return this.isDualPane;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      this.onBackPressed();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }
}
