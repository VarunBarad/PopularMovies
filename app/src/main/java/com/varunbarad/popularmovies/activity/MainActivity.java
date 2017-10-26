package com.varunbarad.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.databinding.ActivityMainBinding;
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener;
import com.varunbarad.popularmovies.fragment.MovieDetailsFragment;
import com.varunbarad.popularmovies.model.data.MovieStub;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
  
  private ActivityMainBinding dataBinding;
  
  private boolean isDualPane;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
  
    this.setSupportActionBar(this.dataBinding.toolbar);
  
    {
      View v = findViewById(R.id.fragment_details_container);
      this.isDualPane = v != null && v.getVisibility() == View.VISIBLE;
    }
  }
  
  @Override
  public void onFragmentInteraction(String data) {
    this.showDetails(MovieStub.getInstance(data));
  }
  
  private void showDetails(MovieStub movie) {
    if (this.isDualPane) {
      MovieDetailsFragment detailsFragment = MovieDetailsFragment.newInstance(movie);
      
      FragmentManager fragmentManager = this.getSupportFragmentManager();
      fragmentManager
          .beginTransaction()
          .replace(R.id.fragment_details_container, detailsFragment)
          .commit();
    } else {
      MovieDetailsActivity.startActivity(this, movie);
    }
  }
}
