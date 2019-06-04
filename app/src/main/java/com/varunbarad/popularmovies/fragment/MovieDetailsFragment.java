package com.varunbarad.popularmovies.fragment;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.varunbarad.popularmovies.R;
import com.varunbarad.popularmovies.adapter.GenreAdapter;
import com.varunbarad.popularmovies.adapter.ReviewAdapter;
import com.varunbarad.popularmovies.adapter.TitledMoviesAdapter;
import com.varunbarad.popularmovies.adapter.TrailerVideoAdapter;
import com.varunbarad.popularmovies.databinding.FragmentMovieDetailsBinding;
import com.varunbarad.popularmovies.eventlistener.FragmentInteractionEvent;
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener;
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener;
import com.varunbarad.popularmovies.model.data.MovieDetails;
import com.varunbarad.popularmovies.model.data.MovieStub;
import com.varunbarad.popularmovies.model.data.Video;
import com.varunbarad.popularmovies.screens.main.MainActivity;
import com.varunbarad.popularmovies.util.Helper;
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiImageHelper;
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiRetroFitHelper;
import com.varunbarad.popularmovies.util.data.MovieContract;
import com.varunbarad.popularmovies.util.data.MovieDbHelper;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator: vbarad
 * Date: 2016-12-11
 * Project: PopularMovies
 */

public class MovieDetailsFragment extends Fragment implements Callback<MovieDetails> {
  private static final String KEY_MOVIE_STUB = "movie_stub";
  
  private MovieDbHelper databaseHelper;
  private OnFragmentInteractionListener fragmentInteractionListener;
  private ProgressDialog progressDialog;
  
  private MovieStub movieStub;
  private MovieDetails movieDetails;
  private boolean isMovieFavorite = false;
  
  private FragmentMovieDetailsBinding dataBinding;
  
  public MovieDetailsFragment() {
    // Required empty public constructor
  }
  
  public static MovieDetailsFragment newInstance(MovieStub movieStub) {
    MovieDetailsFragment fragment = new MovieDetailsFragment();
    Bundle args = new Bundle();
    args.putString(KEY_MOVIE_STUB, movieStub.toString());
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      String movieStubJson = getArguments().getString(KEY_MOVIE_STUB);
      this.movieStub = MovieStub.getInstance(movieStubJson);
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
  public void onResume() {
    super.onResume();
    
    if (this.getActivity() instanceof MainActivity) {
      MainActivity activity = (MainActivity) this.getActivity();
      if ((!activity.checkDualPane()) && (activity.getSupportActionBar() != null)) {
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      }
    }
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    this.fragmentInteractionListener = null;
  }
  
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    this.dataBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false);
    
    this.databaseHelper = new MovieDbHelper(this.getContext());
    
    this.fetchMovieDetails();
    this.fillPartialDetails(this.movieStub);
    
    Cursor cursor = this.databaseHelper.queryMovieDetails(this.movieStub.getId());
    if (cursor != null) {
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        this.movieDetails = Helper.readOneMovie(cursor);
        this.isMovieFavorite = (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_IS_FAVORITE)) == 1);
        cursor.moveToNext();
        this.fillDetails(this.movieDetails);
      } else {
        this.showProgressDialog();
      }
      cursor.close();
    } else {
      this.showProgressDialog();
    }
    
    return this.dataBinding.getRoot();
  }
  
  @Override
  public void onDestroyView() {
    if (this.databaseHelper != null) {
      this.databaseHelper.close();
    }
    super.onDestroyView();
  }
  
  private void showProgressDialog() {
    this.dismissProgressDialog();
  
    this.progressDialog = new ProgressDialog(this.dataBinding.getRoot().getContext());
    this.progressDialog.setIndeterminate(true);
    this.progressDialog.setTitle("Fetching content");
    this.progressDialog.setMessage("Please wait a while...");
    this.progressDialog.setCancelable(false);
    this.progressDialog.show();
  }
  
  private void dismissProgressDialog() {
    if (this.progressDialog != null && this.progressDialog.isShowing()) {
      this.progressDialog.dismiss();
      this.progressDialog = null;
    }
  }
  
  private void fetchMovieDetails() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    
    MovieDbApiRetroFitHelper movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper.class);
    
    movieDbApiRetroFitHelper
        .getMovieDetails(this.movieStub.getId())
        .enqueue(this);
  }
  
  public long getMovieId() {
    if (this.movieDetails != null) {
      return this.movieDetails.getId();
    } else {
      return this.movieStub.getId();
    }
  }
  
  private void fillPartialDetails(MovieStub movieStub) {
    this.dataBinding.textViewMovieDetailsTitle.setText(movieStub.getTitle());
    
    String posterUrl = MovieDbApiImageHelper.getImageUrl(
        movieStub.getPosterPath(),
        Helper.getScreenWidth(this.dataBinding.imageViewMovieDetailsPoster) / 2
    );
    
    Picasso
        .with(this.getContext())
        .load(posterUrl)
        .error(R.drawable.ic_cloud_off)
        .into(this.dataBinding.imageViewMovieDetailsPoster);
    
    String backdropUrl = MovieDbApiImageHelper.getImageUrl(
        movieStub.getBackdropPath(),
        Helper.getScreenWidth(this.dataBinding.imageViewMovieDetailsBackdrop)
    );
    
    Picasso.with(this.getContext())
        .load(backdropUrl)
        .error(R.drawable.ic_cloud_off)
        .into(this.dataBinding.imageViewMovieDetailsBackdrop);
  }
  
  private void fillDetails(final MovieDetails movie) {
    if (this.isMovieFavorite) {
      this.dataBinding.floatingActionButtonMovieDetailsFavorite.setImageResource(R.drawable.ic_favorite);
    } else {
      this.dataBinding.floatingActionButtonMovieDetailsFavorite.setImageResource(R.drawable.ic_favorite_border);
    }
    this.dataBinding.floatingActionButtonMovieDetailsFavorite.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MovieDetailsFragment.this.toggleFavorite();
      }
    });
    
    if (movie.getVideos().getResults().isEmpty() || (this.getVideoUrl(movie) == null)) {
      this.dataBinding.imageButtonMovieDetailsVideos.setVisibility(View.GONE);
    } else {
      this.dataBinding.imageButtonMovieDetailsVideos.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Helper.openYouTubeVideo(MovieDetailsFragment.this.getVideoUrl(movie), MovieDetailsFragment.this.getContext());
        }
      });
    }
    
    this.dataBinding.textViewMovieDetailsOverview.setText(movie.getOverview());
    
    this.dataBinding.linearLayoutMovieDetailsAdult.setVisibility(movie.isAdult() ? View.VISIBLE : View.GONE);
    
    this.dataBinding.textViewMovieDetailsRuntime.setText(String.valueOf(movie.getRuntime()));
    
    if (movie.getReviews().getResults().isEmpty()) {
      this.dataBinding.linearLayoutMovieDetailsReviews.setVisibility(View.GONE);
    } else {
      this.dataBinding.linearLayoutMovieDetailsReviews.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          MovieDetailsFragment.this.showReviews();
        }
      });
    }
    
    if ((movie.getHomepage() == null) || movie.getHomepage().trim().isEmpty()) {
      this.dataBinding.linearLayoutMovieDetailsWebsite.setVisibility(View.GONE);
    } else {
      this.dataBinding.linearLayoutMovieDetailsWebsite.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Helper.openUrlInBrowser(
              movie.getHomepage(),
              view.getContext()
          );
        }
      });
    }
    
    if (movie.getVideos().getResults().isEmpty()) {
      this.dataBinding.linearLayoutMovieDetailsVideos.setVisibility(View.GONE);
    } else {
      this.dataBinding.linearLayoutMovieDetailsVideos.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          MovieDetailsFragment.this.showVideos();
        }
      });
    }
    
    this.dataBinding.textViewMovieDetailsReleaseDate.setText(movie.getReleaseDate());
    
    this.dataBinding.ratingBarMovieDetailsRating.setRating((float) (movie.getAverageVote() / 2.0d));
    
    this.dataBinding.textViewMovieDetailsRating.setText(String.format(Locale.getDefault(), "%.1f (%d)", ((float) (movie.getAverageVote() / 2.0d)), movie.getNumberOfVotes()));
    
    this.dataBinding.recyclerViewMovieDetailsGenres.setLayoutManager(new LinearLayoutManager(
        this.dataBinding.recyclerViewMovieDetailsGenres.getContext(),
        LinearLayoutManager.HORIZONTAL,
        false
    ));
    this.dataBinding.recyclerViewMovieDetailsGenres.setAdapter(new GenreAdapter(movie.getGenres(), new ListItemClickListener() {
      @Override
      public void onItemClick(int position) {
        Toast.makeText(getContext(), String.format(Locale.getDefault(), "%s selected", movie.getGenres().get(position).getName()), Toast.LENGTH_SHORT).show();
      }
    }));
    
    this.dataBinding.recyclerViewMovieDetailsSimilarMovies.setHasFixedSize(true);
    this.dataBinding.recyclerViewMovieDetailsSimilarMovies.setNestedScrollingEnabled(false);
    this.dataBinding.recyclerViewMovieDetailsSimilarMovies.setLayoutManager(new LinearLayoutManager(
        this.dataBinding.recyclerViewMovieDetailsSimilarMovies.getContext(),
        LinearLayoutManager.HORIZONTAL,
        false
    ));
    this.dataBinding.recyclerViewMovieDetailsSimilarMovies.setAdapter(new TitledMoviesAdapter(
        movie.getSimilarMovies().getResults(),
        new ListItemClickListener() {
          @Override
          public void onItemClick(int position) {
            MovieDetailsFragment.this.fragmentInteractionListener.onFragmentInteraction(
                new FragmentInteractionEvent.OpenMovieDetailsEvent(
                    movie.getSimilarMovies().getResults().get(position)
                )
            );
          }
        }
    ));
    
    this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.setHasFixedSize(true);
    this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.setNestedScrollingEnabled(false);
    this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.setLayoutManager(new LinearLayoutManager(
        this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.getContext(),
        LinearLayoutManager.HORIZONTAL,
        false
    ));
    this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.setAdapter(new TitledMoviesAdapter(
        movie.getRecommendations().getResults(),
        new ListItemClickListener() {
          @Override
          public void onItemClick(int position) {
            MovieDetailsFragment.this.fragmentInteractionListener.onFragmentInteraction(
                new FragmentInteractionEvent.OpenMovieDetailsEvent(
                    movie.getRecommendations().getResults().get(position)
                )
            );
          }
        }
    ));
  }
  
  @Override
  public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
    if (this.isVisible()) {
      if (response == null || response.body() == null) {
        this.onFailure(call, new NetworkErrorException("Null response from server"));
      } else {
        this.movieDetails = response.body();
        this.dismissProgressDialog();
        this.fillDetails(this.movieDetails);
  
        // Save the movie-details to database
        this.databaseHelper.insertMovieDetails(this.movieDetails, this.isMovieFavorite);
      }
    }
  }
  
  @Override
  public void onFailure(Call<MovieDetails> call, Throwable t) {
    Toast.makeText(this.dataBinding.getRoot().getContext(), "Network failure", Toast.LENGTH_SHORT).show();
    this.dismissProgressDialog();
  }
  
  private String getVideoUrl(MovieDetails movie) {
    ArrayList<Video> videos = movie.getVideos().getResults();
    String videoUrl = null;
    
    for (Video v : videos) {
      if ((v != null) && (v.getVideoUrl() != null)) {
        videoUrl = v.getVideoUrl();
        break;
      }
    }
    
    return videoUrl;
  }
  
  private void showReviews() {
    ReviewAdapter reviewAdapter = new ReviewAdapter(this.getContext(), this.movieDetails.getReviews().getResults());
    
    MaterialDialog reviewDialog =
        new MaterialDialog.Builder(this.getContext())
            .title(R.string.label_reviews)
            .iconRes(R.drawable.ic_account_circle)
            .limitIconToDefaultSize()
            .adapter(reviewAdapter, new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false))
            .cancelable(true)
            .neutralText(R.string.label_ok)
            .build();
    
    reviewDialog.show();
  }
  
  private void showVideos() {
    TrailerVideoAdapter trailerVideoAdapter = new TrailerVideoAdapter(this.getContext(), this.movieDetails.getVideos().getResults());
    
    MaterialDialog reviewDialog =
        new MaterialDialog.Builder(this.getContext())
            .title(R.string.label_view_videos)
            .iconRes(R.drawable.ic_video_library)
            .limitIconToDefaultSize()
            .adapter(trailerVideoAdapter, new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false))
            .cancelable(true)
            .neutralText(R.string.label_ok)
            .build();
    
    reviewDialog.show();
  }
  
  private void toggleFavorite() {
    if (this.isMovieFavorite) {
      this.isMovieFavorite = false;
      this.dataBinding.floatingActionButtonMovieDetailsFavorite.setImageResource(R.drawable.ic_favorite_border);
  
      this.databaseHelper.updateMovieFavoriteStatus(
          this.movieDetails.getId(),
          this.isMovieFavorite
      );
  
      this.fragmentInteractionListener.onFragmentInteraction(
          new FragmentInteractionEvent.RemoveFromFavoriteEvent(this.movieDetails.getId())
      );
    } else {
      this.isMovieFavorite = true;
      this.dataBinding.floatingActionButtonMovieDetailsFavorite.setImageResource(R.drawable.ic_favorite);
  
      this.databaseHelper.updateMovieFavoriteStatus(
          this.movieDetails.getId(),
          this.isMovieFavorite
      );
  
      this.fragmentInteractionListener.onFragmentInteraction(
          new FragmentInteractionEvent.AddToFavoriteEvent(
              Helper.movieStubFromMovieDetails(this.movieDetails)
          )
      );
    }
  }
}
