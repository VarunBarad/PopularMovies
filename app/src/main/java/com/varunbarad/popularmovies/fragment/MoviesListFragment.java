package com.varunbarad.popularmovies.fragment;

import android.content.Context;
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

import com.varunbarad.popularmovies.BuildConfig;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.adapter.MoviesAdapter;
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

  private View rootView;

  private RecyclerView moviesRecyclerView;
  private RecyclerView.LayoutManager moviesLayoutManager;
  private MoviesAdapter moviesAdapter;

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
    this.rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);

    this.moviesRecyclerView = (RecyclerView) this.rootView.findViewById(R.id.recyclerView_movies);
    this.moviesRecyclerView.setHasFixedSize(true);

    this.moviesLayoutManager = new GridLayoutManager(this.getContext(), 2, LinearLayoutManager.VERTICAL, false);
    this.moviesRecyclerView.setLayoutManager(this.moviesLayoutManager);

    if (Helper.isConnectedToInternet(this.getContext())) {
      this.fetchPopularMovies();
    } else {
      this.rootView
          .findViewById(R.id.placeholder_progress)
          .setVisibility(View.GONE);
      this.moviesRecyclerView
          .setVisibility(View.GONE);
      this.rootView
          .findViewById(R.id.placeHolder_error)
          .setVisibility(View.VISIBLE);
    }

    return this.rootView;
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
                .getTitle()
        );
  }

  private void fetchPopularMovies() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    MovieDbApiRetroFitHelper movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper.class);

    movieDbApiRetroFitHelper
        .getPopularMovies(1, BuildConfig.TMDBApiKey)
        .enqueue(this);
  }

  @Override
  public void onResponse(Call<MovieList> call, Response<MovieList> response) {
    this.moviesAdapter = new MoviesAdapter(response.body().getResults(), this);
    this.moviesRecyclerView.setAdapter(this.moviesAdapter);
    this.rootView
        .findViewById(R.id.placeholder_progress)
        .setVisibility(View.GONE);
    this.moviesRecyclerView
        .setVisibility(View.VISIBLE);
  }

  @Override
  public void onFailure(Call<MovieList> call, Throwable t) {
    this.rootView
        .findViewById(R.id.placeholder_progress)
        .setVisibility(View.GONE);
    this.moviesRecyclerView
        .setVisibility(View.GONE);
    this.rootView
        .findViewById(R.id.placeHolder_error)
        .setVisibility(View.VISIBLE);

    Snackbar
        .make(this.rootView, "Network Error", Snackbar.LENGTH_SHORT)
        .show();
  }
}
