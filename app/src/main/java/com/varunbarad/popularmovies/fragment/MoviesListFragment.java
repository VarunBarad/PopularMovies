package com.varunbarad.popularmovies.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.activity.MainActivity;
import com.varunbarad.popularmovies.adapter.MoviesAdapter;
import com.varunbarad.popularmovies.databinding.FragmentMoviesListBinding;
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener;
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener;
import com.varunbarad.popularmovies.model.data.MovieList;
import com.varunbarad.popularmovies.model.data.MovieStub;
import com.varunbarad.popularmovies.util.Helper;
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiRetroFitHelper;
import com.varunbarad.popularmovies.util.data.MovieContract;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesListFragment extends Fragment implements ListItemClickListener {
  private static final long ACCEPTABLE_DELAY = 10 * 60 * 1000;
  
  private OnFragmentInteractionListener fragmentInteractionListener;
  
  private FragmentMoviesListBinding dataBinding;
  
  private RecyclerView.LayoutManager moviesLayoutManager;
  private MoviesAdapter moviesAdapter;
  
  private String sortOrder;
  private String[] sortCriteriaEntries;
  
  public MoviesListFragment() {
    // Required empty public constructor
  }
  
  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment MoviesListFragment.
   */
  public static MoviesListFragment newInstance() {
    MoviesListFragment fragment = new MoviesListFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
  
    }
  }
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      this.fragmentInteractionListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    this.fragmentInteractionListener = null;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.sortCriteriaEntries = this.getContext().getResources().getStringArray(R.array.entries_sortCriteria);
    
    this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies_list, container, false);
    
    this.dataBinding.spinnerSortCriteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if ((MoviesListFragment.this.sortOrder == null) || (!MoviesListFragment.this.sortOrder.equals(sortCriteriaEntries[position]))) {
          MoviesListFragment.this.sortOrder = sortCriteriaEntries[position];
  
          if (MoviesListFragment.this.sortOrder.equalsIgnoreCase("most popular")) {
            if (MoviesListFragment.this.isPopularRefreshNeeded()) {
              if (Helper.isConnectedToInternet(MoviesListFragment.this.getContext())) {
                MoviesListFragment.this.fetchPopularMovies();
              } else {
                MoviesListFragment.this.showNetworkError();
              }
            } else {
              MoviesListFragment.this.showMovies(MoviesListFragment.this.retreivePopularMovies());
            }
          } else if (MoviesListFragment.this.sortOrder.equalsIgnoreCase("highest rated")) {
            if (MoviesListFragment.this.isHighestRatedRefreshNeeded()) {
              if (Helper.isConnectedToInternet(MoviesListFragment.this.getContext())) {
                MoviesListFragment.this.fetchHighestRatedMovies();
              } else {
                MoviesListFragment.this.showNetworkError();
              }
            } else {
              MoviesListFragment.this.showMovies(MoviesListFragment.this.retreiveHighestRatedMovies());
            }
          } else if (MoviesListFragment.this.sortOrder.equalsIgnoreCase("my favorites")) {
            MoviesListFragment.this.fetchFavoriteMovies();
          }
        }
      }
      
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        
      }
    });
  
    this.dataBinding.recyclerViewMovies.setHasFixedSize(true);
    this.moviesLayoutManager = new GridLayoutManager(this.getContext(), 2, LinearLayoutManager.VERTICAL, false);
    this.dataBinding.recyclerViewMovies.setLayoutManager(this.moviesLayoutManager);
  
    this.sortOrder = this.sortCriteriaEntries[this.dataBinding.spinnerSortCriteria.getSelectedItemPosition()];
  
    if (this.sortOrder.equalsIgnoreCase("most popular")) {
      if (this.isPopularRefreshNeeded()) {
        if (Helper.isConnectedToInternet(this.getContext())) {
          this.fetchPopularMovies();
        } else {
          this.showNetworkError();
        }
      } else {
        this.showMovies(this.retreivePopularMovies());
      }
    } else if (this.sortOrder.equalsIgnoreCase("highest rated")) {
      if (this.isHighestRatedRefreshNeeded()) {
        if (Helper.isConnectedToInternet(this.getContext())) {
          this.fetchHighestRatedMovies();
        } else {
          this.showNetworkError();
        }
      } else {
        this.showMovies(this.retreiveHighestRatedMovies());
      }
    } else if (this.sortOrder.equalsIgnoreCase("my favorites")) {
      this.fetchFavoriteMovies();
    }
    
    return this.dataBinding.getRoot();
  }
  
  @Override
  public void onResume() {
    super.onResume();
  
    if (this.getActivity() instanceof MainActivity) {
      MainActivity activity = (MainActivity) this.getActivity();
      if (activity.getSupportActionBar() != null) {
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      }
    }
    
    if (this.dataBinding.spinnerSortCriteria.getSelectedItem().toString().equalsIgnoreCase("my favorites")) {
      Cursor cursor = this.getContext().getContentResolver().query(
          MovieContract.Movie.FAVORITES_URI,
          null,
          null,
          null,
          null
      );
      if (cursor != null) {
        if (cursor.getCount() < 1) {
          this.showNoFavorites();
        }
        cursor.close();
      }
    }
  }
  
  @Override
  public void onItemClick(int position) {
    this
        .fragmentInteractionListener
        .onFragmentInteraction(
            Helper.generateMessage(
                OnFragmentInteractionListener.TAG_MOVIE,
                this.moviesAdapter.getMovies().get(position).toString()
            )
        );
  }
  
  private void fetchPopularMovies() {
    this.showProgress();
    
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    
    MovieDbApiRetroFitHelper movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper.class);
    
    movieDbApiRetroFitHelper
        .getPopularMovies(1)
        .enqueue(new Callback<MovieList>() {
          @Override
          public void onResponse(Call<MovieList> call, Response<MovieList> response) {
            MoviesListFragment.this.showMovies(response.body().getResults());
            
            MoviesListFragment.this.storePopularMovies(response.body().getResults());
          }
          
          @Override
          public void onFailure(Call<MovieList> call, Throwable t) {
            MoviesListFragment.this.showNetworkError();
          }
        });
  }
  
  private void fetchHighestRatedMovies() {
    this.showProgress();
  
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    
    MovieDbApiRetroFitHelper movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper.class);
    
    movieDbApiRetroFitHelper
        .getHighestRatedMovies(1)
        .enqueue(new Callback<MovieList>() {
          @Override
          public void onResponse(Call<MovieList> call, Response<MovieList> response) {
            MoviesListFragment.this.showMovies(response.body().getResults());
  
            MoviesListFragment.this.storeHighestRatedMovies(response.body().getResults());
          }
  
          @Override
          public void onFailure(Call<MovieList> call, Throwable t) {
            MoviesListFragment.this.showNetworkError();
          }
        });
  }
  
  private void fetchFavoriteMovies() {
    Cursor cursor = this.getContext().getContentResolver().query(MovieContract.Movie.FAVORITES_URI, null, null, null, null, null);
    
    if (cursor != null) {
      if (cursor.getCount() > 0) {
        ArrayList<MovieStub> favoriteMovies = new ArrayList<>(cursor.getCount());
        
        cursor.moveToFirst();
        do {
          favoriteMovies.add(Helper.movieStubFromMovieDetails(Helper.readOneMovie(cursor)));
        } while (cursor.moveToNext());
  
        this.showMovies(favoriteMovies);
      } else {
        this.showNoFavorites();
      }
      
      cursor.close();
    } else {
      this.showNoFavorites();
    }
  }
  
  private void showNetworkError() {
    this.dataBinding.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.recyclerViewMovies
        .setVisibility(View.GONE);
    this.dataBinding.placeHolderError
        .setVisibility(View.VISIBLE);
    this.dataBinding.placeHolderNoFavorites
        .setVisibility(View.GONE);
  }
  
  private void showProgress() {
    this.dataBinding.placeholderProgress
        .setVisibility(View.VISIBLE);
    this.dataBinding.recyclerViewMovies
        .setVisibility(View.GONE);
    this.dataBinding.placeHolderError
        .setVisibility(View.GONE);
    this.dataBinding.placeHolderNoFavorites
        .setVisibility(View.GONE);
  }
  
  private void showNoFavorites() {
    this.dataBinding.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.recyclerViewMovies
        .setVisibility(View.GONE);
    this.dataBinding.placeHolderError
        .setVisibility(View.GONE);
    this.dataBinding.placeHolderNoFavorites
        .setVisibility(View.VISIBLE);
  }
  
  private void showMovies(ArrayList<MovieStub> movies) {
    this.moviesAdapter = new MoviesAdapter(movies, this);
    this.dataBinding.recyclerViewMovies.setAdapter(this.moviesAdapter);
    
    this.dataBinding.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.recyclerViewMovies
        .setVisibility(View.VISIBLE);
    this.dataBinding.placeHolderError
        .setVisibility(View.GONE);
    this.dataBinding.placeHolderNoFavorites
        .setVisibility(View.GONE);
  }
  
  public void addFavorite(MovieStub movieStub) {
    if (this.sortOrder.equalsIgnoreCase("my favorites")) {
      if (this.dataBinding.recyclerViewMovies.getVisibility() == View.VISIBLE) {
        this.moviesAdapter.addMovie(movieStub);
      } else {
        this.fetchFavoriteMovies();
      }
    }
  }
  
  public void removeFavorite(long movieId) {
    if (this.sortOrder.equalsIgnoreCase("my favorites")) {
      this.moviesAdapter.removeMovie(movieId);
      if (this.moviesAdapter.getItemCount() < 1) {
        this.showNoFavorites();
      }
    }
  }
  
  private boolean isPopularRefreshNeeded() {
    SharedPreferences preferences = this.getContext().getSharedPreferences(this.getContext().getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
    
    long lastLoadTime = preferences.getLong(this.getContext().getString(R.string.PREFS_KEY_POPULAR_TIMESTAMP), 0);
    return ((System.currentTimeMillis() - lastLoadTime) > MoviesListFragment.ACCEPTABLE_DELAY);
  }
  
  private boolean isHighestRatedRefreshNeeded() {
    SharedPreferences preferences = this.getContext().getSharedPreferences(this.getContext().getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
    
    long lastLoadTime = preferences.getLong(this.getContext().getString(R.string.PREFS_KEY_HIGHEST_RATED_TIMESTAMP), 0);
    return ((System.currentTimeMillis() - lastLoadTime) > MoviesListFragment.ACCEPTABLE_DELAY);
  }
  
  private void storePopularMovies(ArrayList<MovieStub> movies) {
    SharedPreferences preferences = this.getContext().getSharedPreferences(this.getContext().getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    
    editor.putString(this.getContext().getString(R.string.PREFS_KEY_POPULAR), new Gson().toJson(movies));
    editor.putLong(this.getContext().getString(R.string.PREFS_KEY_POPULAR_TIMESTAMP), System.currentTimeMillis());
    editor.apply();
  }
  
  private void storeHighestRatedMovies(ArrayList<MovieStub> movies) {
    SharedPreferences preferences = this.getContext().getSharedPreferences(this.getContext().getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    
    editor.putString(this.getContext().getString(R.string.PREFS_KEY_HIGHEST_RATED), new Gson().toJson(movies));
    editor.putLong(this.getContext().getString(R.string.PREFS_KEY_HIGHEST_RATED_TIMESTAMP), System.currentTimeMillis());
    editor.apply();
  }
  
  private ArrayList<MovieStub> retreivePopularMovies() {
    SharedPreferences preferences = this.getContext().getSharedPreferences(this.getContext().getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
    
    return new ArrayList<>(Arrays.asList(new Gson().fromJson(preferences.getString(this.getContext().getString(R.string.PREFS_KEY_POPULAR), null), MovieStub[].class)));
  }
  
  private ArrayList<MovieStub> retreiveHighestRatedMovies() {
    SharedPreferences preferences = this.getContext().getSharedPreferences(this.getContext().getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
    
    return new ArrayList<>(Arrays.asList(new Gson().fromJson(preferences.getString(this.getContext().getString(R.string.PREFS_KEY_HIGHEST_RATED), null), MovieStub[].class)));
  }
}
