package com.varunbarad.popularmovies.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.adapter.MoviesAdapter;
import com.varunbarad.popularmovies.databinding.FragmentMoviesListBinding;
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener;
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener;
import com.varunbarad.popularmovies.model.data.MovieList;
import com.varunbarad.popularmovies.util.Helper;
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiRetroFitHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesListFragment extends Fragment implements ListItemClickListener, Callback<MovieList> {
  private OnFragmentInteractionListener fragmentInteractionListener;
  
  private FragmentMoviesListBinding dataBinding;

  private RecyclerView.LayoutManager moviesLayoutManager;
  private MoviesAdapter moviesAdapter;
  
  private String sortOrder;

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
    this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies_list, container, false);
  
    this.dataBinding.spinnerSortCriteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      private final String[] sortCriteriaEntries = getContext().getResources().getStringArray(R.array.entries_sortCriteria);
    
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if ((MoviesListFragment.this.sortOrder == null) || (!MoviesListFragment.this.sortOrder.equals(sortCriteriaEntries[position]))) {
          MoviesListFragment.this.sortOrder = sortCriteriaEntries[position];
        
          if (sortCriteriaEntries[position].equalsIgnoreCase("most popular")) {
            if (Helper.isConnectedToInternet(MoviesListFragment.this.getContext())) {
              MoviesListFragment.this.fetchPopularMovies();
            } else {
              MoviesListFragment.this.showNetworkError();
            }
          } else if (sortCriteriaEntries[position].equalsIgnoreCase("highest rated")) {
            if (Helper.isConnectedToInternet(MoviesListFragment.this.getContext())) {
              MoviesListFragment.this.fetchHighestRatedMovies();
            } else {
              MoviesListFragment.this.showNetworkError();
            }
          } else if (sortCriteriaEntries[position].equalsIgnoreCase("my favorites")) {
            // ToDo: Display favorites
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

    if (Helper.isConnectedToInternet(this.getContext())) {
      // ToDo: Open the sort criteria which was open the last time
      this.fetchPopularMovies();
    } else {
      this.showNetworkError();
    }
  
    return this.dataBinding.getRoot();
  }

  @Override
  public void onItemClick(int position) {
    this
        .fragmentInteractionListener
        .onFragmentInteraction(
            this
                .moviesAdapter
                .getMovies()
                .get(position)
                .toString()
        );
  }

  private void fetchPopularMovies() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    MovieDbApiRetroFitHelper movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper.class);

    movieDbApiRetroFitHelper
        .getPopularMovies(1)
        .enqueue(this);
  }
  
  private void fetchHighestRatedMovies() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    
    MovieDbApiRetroFitHelper movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper.class);
    
    movieDbApiRetroFitHelper
        .getHighestRatedMovies(1)
        .enqueue(this);
  }

  @Override
  public void onResponse(Call<MovieList> call, Response<MovieList> response) {
    this.moviesAdapter = new MoviesAdapter(response.body().getResults(), this);
    this.dataBinding.recyclerViewMovies.setAdapter(this.moviesAdapter);
    this.dataBinding.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.recyclerViewMovies
        .setVisibility(View.VISIBLE);
  }

  @Override
  public void onFailure(Call<MovieList> call, Throwable t) {
    this.dataBinding.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.recyclerViewMovies
        .setVisibility(View.GONE);
    this.dataBinding.placeHolderError
        .setVisibility(View.VISIBLE);

    Snackbar
        .make(this.dataBinding.getRoot(), "Network Error", Snackbar.LENGTH_SHORT)
        .show();
  }
  
  private void showNetworkError() {
    this.dataBinding.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.recyclerViewMovies
        .setVisibility(View.GONE);
    this.dataBinding.placeHolderError
        .setVisibility(View.VISIBLE);
  }
}
