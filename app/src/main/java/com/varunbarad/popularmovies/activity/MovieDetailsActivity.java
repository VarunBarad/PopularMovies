package com.varunbarad.popularmovies.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener;
import com.varunbarad.popularmovies.fragment.MovieDetailsFragment;
import com.varunbarad.popularmovies.model.data.MovieStub;

public class MovieDetailsActivity extends AppCompatActivity implements OnFragmentInteractionListener {
  public static final String ARGS_MOVIE_STUB = "movie_stub";
  
  private ActivityMovieDetailsBinding dataBinding;
  
  public static void startActivity(Activity parentActivity, MovieStub movie) {
    Intent intent = new Intent(parentActivity, MovieDetailsActivity.class);
    intent.putExtra(ARGS_MOVIE_STUB, movie.toString());
    parentActivity.startActivity(intent);
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
    
    this.setSupportActionBar(this.dataBinding.toolbar);
    
    ActionBar actionBar = this.getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    MovieStub movieStub = MovieStub.getInstance(this.getIntent().getStringExtra(ARGS_MOVIE_STUB));
    
    MovieDetailsFragment detailsFragment = MovieDetailsFragment.newInstance(movieStub);
    
    FragmentManager fragmentManager = this.getSupportFragmentManager();
    
    if (fragmentManager.findFragmentById(R.id.fragment_details_container) != null) {
      fragmentManager
          .beginTransaction()
          .replace(R.id.fragment_details_container, detailsFragment)
          .commit();
    } else {
      fragmentManager
          .beginTransaction()
          .add(R.id.fragment_details_container, detailsFragment)
          .commit();
    }
  }
  
  @Override
  public void onFragmentInteraction(String data) {
    this.setActivityTitle(data);
  }
  
  private void setActivityTitle(String title) {
    if (title != null && !title.isEmpty()) {
      this.dataBinding.toolbar.setTitle(title);
    }
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
