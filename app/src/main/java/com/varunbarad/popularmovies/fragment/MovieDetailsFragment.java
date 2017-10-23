package com.varunbarad.popularmovies.fragment;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
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
import com.varunbarad.popularmovies.activity.MovieDetailsActivity;
import com.varunbarad.popularmovies.adapter.GenreAdapter;
import com.varunbarad.popularmovies.adapter.ReviewAdapter;
import com.varunbarad.popularmovies.adapter.TitledMoviesAdapter;
import com.varunbarad.popularmovies.adapter.TrailerVideoAdapter;
import com.varunbarad.popularmovies.databinding.FragmentMovieDetailsBinding;
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener;
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener;
import com.varunbarad.popularmovies.model.data.MovieDetails;
import com.varunbarad.popularmovies.model.data.MovieStub;
import com.varunbarad.popularmovies.model.data.Video;
import com.varunbarad.popularmovies.util.Helper;
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiImageHelper;
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiRetroFitHelper;

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
  
  private OnFragmentInteractionListener fragmentInteractionListener;
  private ProgressDialog progressDialog;
  
  private MovieStub movieStub;
  private MovieDetails movieDetails;
  
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
  public void onDetach() {
    super.onDetach();
    this.fragmentInteractionListener = null;
  }
  
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false);
    
    this.fetchMovieDetails();
    this.fillPartialDetails(this.movieStub);
    this.showProgressDialog();
    
    return this.dataBinding.getRoot();
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
  
  private void fillPartialDetails(MovieStub movieStub) {
    this.dataBinding.textViewMovieDetailsTitle.setText(movieStub.getTitle());
    
    String posterUrl = MovieDbApiImageHelper.getImageUrl(
        movieStub.getPosterPath(),
        Helper.getScreenWidth(this.dataBinding.imageViewMovieDetailsPoster) / 2
    );
    
    Picasso
        .with(this.getContext())
        .load(posterUrl)
        .error(R.drawable.ic_cloud_off_black)
        .into(this.dataBinding.imageViewMovieDetailsPoster);
    
    String backdropUrl = MovieDbApiImageHelper.getImageUrl(
        movieStub.getBackdropPath(),
        Helper.getScreenWidth(this.dataBinding.imageViewMovieDetailsBackdrop)
    );
    
    Picasso.with(this.getContext())
        .load(backdropUrl)
        .error(R.drawable.ic_cloud_off_black)
        .into(this.dataBinding.imageViewMovieDetailsBackdrop);
  }
  
  private void fillDetails(final MovieDetails movie) {
    this.dataBinding.floatingActionButtonMovieDetailsFavorite.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // ToDo: Change favorite state
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
            MovieDetailsActivity.startActivity(getActivity(), movie.getSimilarMovies().getResults().get(position));
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
            MovieDetailsActivity.startActivity(getActivity(), movie.getRecommendations().getResults().get(position));
          }
        }
    ));
  }
  
  @Override
  public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
    if (response == null || response.body() == null) {
      this.onFailure(call, new NetworkErrorException("Null response from server"));
    } else {
      this.movieDetails = response.body();
      this.dismissProgressDialog();
      this.fillDetails(this.movieDetails);
    }
  }
  
  @Override
  public void onFailure(Call<MovieDetails> call, Throwable t) {
    // ToDo: Pass message to users on failure
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
}