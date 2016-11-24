package com.varunbarad.popularmovies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.varunbarad.popularmovies.BuildConfig;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.model.data.MovieList;
import com.varunbarad.popularmovies.util.MovieDbApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

  private TextView v;

  public MainActivityFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    this.v = (TextView) view.findViewById(R.id.textView_placeHolder);

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(MovieDbApi.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    MovieDbApi movieDbApi = retrofit.create(MovieDbApi.class);

    movieDbApi
        .getPopularMovies(1, BuildConfig.TMDBApiKey)
        .enqueue(new Callback<MovieList>() {
          @Override
          public void onResponse(Call<MovieList> call, Response<MovieList> response) {
            v.setText(response.body().toString());
          }

          @Override
          public void onFailure(Call<MovieList> call, Throwable t) {
            v.setText(t.getMessage());
          }
        });

    return view;
  }
}
