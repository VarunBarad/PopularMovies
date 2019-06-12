package com.varunbarad.popularmovies.screens.main.movie_details

import android.accounts.NetworkErrorException
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.varunbarad.popularmovies.R
import com.varunbarad.popularmovies.adapter.GenreAdapter
import com.varunbarad.popularmovies.adapter.ReviewAdapter
import com.varunbarad.popularmovies.adapter.TitledMoviesAdapter
import com.varunbarad.popularmovies.adapter.TrailerVideoAdapter
import com.varunbarad.popularmovies.databinding.FragmentMovieDetailsBinding
import com.varunbarad.popularmovies.eventlistener.FragmentInteractionEvent
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener
import com.varunbarad.popularmovies.model.data.MovieDetails
import com.varunbarad.popularmovies.model.data.MovieStub
import com.varunbarad.popularmovies.screens.main.MainActivity
import com.varunbarad.popularmovies.util.Helper
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiRetroFitHelper
import com.varunbarad.popularmovies.util.MovieDbApi.getImageUrl
import com.varunbarad.popularmovies.util.data.MovieContract
import com.varunbarad.popularmovies.util.data.MovieDbHelper
import com.varunbarad.popularmovies.util.readOneMovie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

/**
 * Creator: Varun Barad
 * Date: 2019-06-04
 * Project: PopularMovies
 */
class MovieDetailsFragment : Fragment(), Callback<MovieDetails> {
    private var fragmentInteractionListener: OnFragmentInteractionListener? = null

    private lateinit var databaseHelper: MovieDbHelper

    private lateinit var dataBinding: FragmentMovieDetailsBinding
    private var progressDialog: ProgressDialog? = null

    private lateinit var movieStub: MovieStub
    private var movieDetails: MovieDetails? = null
    private var isMovieFavorite: Boolean = false

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = this.arguments
        if (arguments != null) {
            val movieStubJson = arguments.getString(KEY_MOVIE_STUB) ?: "{}"
            this.movieStub = MovieStub.getInstance(movieStubJson)!!
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            this.fragmentInteractionListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()

        val activity = this.activity
        if (activity is MainActivity) {
            if (!activity.checkDualPane()) {
                activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.fragmentInteractionListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.dataBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        this.databaseHelper = MovieDbHelper(this.requireContext())

        this.movieStub
        this.fetchMovieDetails(this.movieStub.id)
        this.fillPartialDetails(this.movieStub)

        val cursor = this.databaseHelper.queryMovieDetails(this.movieStub.id)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                val movieDetails = cursor.readOneMovie()
                this.movieDetails = movieDetails
                this.isMovieFavorite =
                    (cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_IS_FAVORITE)) == 1)
                cursor.moveToNext()
                this.fillDetails(movieDetails)
            } else {
                this.showProgressDialog()
            }

            cursor.close()
        } else {
            this.showProgressDialog()
        }

        return this.dataBinding.root
    }

    override fun onDestroyView() {
        this.databaseHelper.close()
        super.onDestroyView()
    }

    private fun showProgressDialog() {
        this.dismissProgressDialog()

        this.progressDialog = ProgressDialog(this.dataBinding.root.context).apply {
            isIndeterminate = true
            setTitle("Fetching content")
            setMessage("Please wait a while...")
            setCancelable(false)
        }
        this.progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        if (this.progressDialog?.isShowing == true) {
            this.progressDialog?.dismiss()
            this.progressDialog = null
        }
    }

    private fun fetchMovieDetails(movieId: Long) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper::class.java)

        movieDbApiRetroFitHelper
            .getMovieDetails(movieId)
            .enqueue(this)
    }

    fun getMovieId(): Long {
        return this.movieDetails?.id ?: this.movieStub.id
    }

    private fun fillPartialDetails(movieStub: MovieStub) {
        this.dataBinding.textViewMovieDetailsTitle.text = movieStub.title

        this.handler.postDelayed({
            Picasso.with(this.dataBinding.imageViewMovieDetailsPoster.context)
                .load(getImageUrl(movieStub.posterPath, this.dataBinding.imageViewMovieDetailsPoster.width))
                .error(R.drawable.ic_cloud_off)
                .into(this.dataBinding.imageViewMovieDetailsPoster)

            Picasso.with(this.dataBinding.imageViewMovieDetailsBackdrop.context)
                .load(getImageUrl(movieStub.backdropPath, this.dataBinding.imageViewMovieDetailsBackdrop.width))
                .error(R.drawable.ic_cloud_off)
                .into(this.dataBinding.imageViewMovieDetailsBackdrop)
        }, 17)
    }

    private fun fillDetails(movie: MovieDetails) {
        if (this.isMovieFavorite) {
            this.dataBinding.floatingActionButtonMovieDetailsFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            this.dataBinding.floatingActionButtonMovieDetailsFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
        this.dataBinding.floatingActionButtonMovieDetailsFavorite.setOnClickListener { this.toggleFavorite() }

        val videoUrl = this.getVideoUrl(movie)
        if (videoUrl != null) {
            this.dataBinding.imageButtonMovieDetailsVideos.visibility = View.VISIBLE
            this.dataBinding.imageButtonMovieDetailsVideos.setOnClickListener {
                Helper.openYouTubeVideo(videoUrl, this.requireContext())
            }
        } else {
            this.dataBinding.imageButtonMovieDetailsVideos.visibility = View.GONE
        }

        this.dataBinding.textViewMovieDetailsOverview.text = movie.overview

        this.dataBinding.linearLayoutMovieDetailsAdult.visibility = if (movie.isAdult) View.VISIBLE else View.GONE

        this.dataBinding.textViewMovieDetailsRuntime.text = movie.runtime.toString()

        if (movie.reviews?.results?.isNotEmpty() == true) {
            this.dataBinding.linearLayoutMovieDetailsReviews.visibility = View.VISIBLE
            this.dataBinding.linearLayoutMovieDetailsReviews.setOnClickListener { this.showReviews() }
        } else {
            this.dataBinding.linearLayoutMovieDetailsReviews.visibility = View.GONE
        }

        if (movie.homepage.isNotBlank()) {
            this.dataBinding.linearLayoutMovieDetailsWebsite.visibility = View.VISIBLE
            this.dataBinding.linearLayoutMovieDetailsWebsite.setOnClickListener {
                Helper.openUrlInBrowser(movie.homepage.trim(), this.requireContext())
            }
        } else {
            this.dataBinding.linearLayoutMovieDetailsWebsite.visibility = View.GONE
        }

        if (movie.videos?.results?.isNotEmpty() == true) {
            this.dataBinding.linearLayoutMovieDetailsVideos.visibility = View.VISIBLE
            this.dataBinding.linearLayoutMovieDetailsVideos.setOnClickListener { this.showVideos() }
        } else {
            this.dataBinding.linearLayoutMovieDetailsVideos.visibility = View.GONE
        }

        this.dataBinding.textViewMovieDetailsReleaseDate.text = movie.releaseDate

        this.dataBinding.ratingBarMovieDetailsRating.rating = (movie.averageVote / 2.0).toFloat()

        this.dataBinding.textViewMovieDetailsRating.text = "%.1f (%d)".format(
            Locale.getDefault(),
            (movie.averageVote / 2.0).toFloat(),
            movie.numberOfVotes
        )

        this.dataBinding.recyclerViewMovieDetailsGenres.layoutManager = LinearLayoutManager(
            this.dataBinding.recyclerViewMovieDetailsGenres.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        this.dataBinding.recyclerViewMovieDetailsGenres.adapter = GenreAdapter(movie.genres) { position ->
            Snackbar.make(
                this.dataBinding.root,
                "${movie.genres[position].name} selected",
                Snackbar.LENGTH_LONG
            ).show()
        }

        this.dataBinding.recyclerViewMovieDetailsSimilarMovies.setHasFixedSize(true)
        this.dataBinding.recyclerViewMovieDetailsSimilarMovies.isNestedScrollingEnabled = false
        this.dataBinding.recyclerViewMovieDetailsSimilarMovies.layoutManager = LinearLayoutManager(
            this.dataBinding.recyclerViewMovieDetailsSimilarMovies.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        this.dataBinding.recyclerViewMovieDetailsSimilarMovies.adapter =
            TitledMoviesAdapter(movie.similarMovies?.results) { position ->
                this.fragmentInteractionListener?.onFragmentInteraction(
                    FragmentInteractionEvent.OpenMovieDetailsEvent(movie.similarMovies?.results?.get(position)!!)
                )
            }

        this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.setHasFixedSize(true)
        this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.isNestedScrollingEnabled = false
        this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.layoutManager = LinearLayoutManager(
            this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        this.dataBinding.recyclerViewMovieDetailsRecommendedMovies.adapter = TitledMoviesAdapter(
            movie.recommendations?.results
        ) { position ->
            this.fragmentInteractionListener?.onFragmentInteraction(
                FragmentInteractionEvent.OpenMovieDetailsEvent(movie.recommendations?.results?.get(position)!!)
            )
        }
    }

    override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
        if (this.isVisible) {
            val movieDetails = response.body()
            if (movieDetails != null) {
                this.movieDetails = movieDetails
                this.dismissProgressDialog()
                this.fillDetails(movieDetails)

                // Save the movie-details to database
                this.databaseHelper.insertMovieDetails(movieDetails, this.isMovieFavorite)
            } else {
                this.onFailure(call, NetworkErrorException("Null response from server"))
            }
        }
    }

    override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
        Snackbar.make(this.dataBinding.root, "Network failure", Snackbar.LENGTH_LONG).show()
        this.dismissProgressDialog()
    }

    private fun getVideoUrl(movie: MovieDetails): String? {
        return movie.videos?.results?.map { it.getVideoUrl() }?.find { it != null }
    }

    private fun showReviews() {
        val reviews = this.movieDetails?.reviews?.results
        if (reviews != null) {
            val reviewAdapter = ReviewAdapter(this.requireContext(), reviews)

            MaterialDialog(this.requireContext()).show {
                title(R.string.label_reviews)
                icon(R.drawable.ic_account_circle)
                cancelable(true)
                negativeButton(R.string.label_ok)
                customListAdapter(
                    reviewAdapter,
                    LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
                )
            }
        }
    }

    private fun showVideos() {
        val videos = this.movieDetails?.videos?.results
        if (videos != null) {
            val trailerVideoAdapter = TrailerVideoAdapter(this.requireContext(), videos)

            MaterialDialog(this.requireContext()).show {
                title(R.string.label_view_videos)
                icon(R.drawable.ic_video_library)
                cancelable(true)
                negativeButton(R.string.label_ok)
                customListAdapter(
                    trailerVideoAdapter,
                    LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
                )
            }
        }
    }

    private fun toggleFavorite() {
        this.isMovieFavorite = !this.isMovieFavorite

        if (this.isMovieFavorite) {
            this.dataBinding.floatingActionButtonMovieDetailsFavorite.setImageResource(R.drawable.ic_favorite)

            val movieDetails = this.movieDetails
            if (movieDetails != null) {
                this.databaseHelper.updateMovieFavoriteStatus(movieDetails.id, true)

                this.fragmentInteractionListener?.onFragmentInteraction(
                    FragmentInteractionEvent.AddToFavoriteEvent(movieDetails.toMovieStub())
                )
            }
        } else {
            this.dataBinding.floatingActionButtonMovieDetailsFavorite.setImageResource(R.drawable.ic_favorite_border)

            val movieId = this.movieDetails?.id
            if (movieId != null) {
                this.databaseHelper.updateMovieFavoriteStatus(movieId, false)

                this.fragmentInteractionListener?.onFragmentInteraction(
                    FragmentInteractionEvent.RemoveFromFavoriteEvent(movieId)
                )
            }
        }
    }

    companion object {
        private const val KEY_MOVIE_STUB = "movie-stub"

        @JvmStatic
        fun newInstance(movieStub: MovieStub): MovieDetailsFragment {
            return MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_MOVIE_STUB, movieStub.toString())
                }
            }
        }
    }
}
