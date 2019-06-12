package com.varunbarad.popularmovies.screens.main.movies_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.varunbarad.popularmovies.R
import com.varunbarad.popularmovies.adapter.MoviesAdapter
import com.varunbarad.popularmovies.databinding.FragmentMoviesListBinding
import com.varunbarad.popularmovies.eventlistener.FragmentInteractionEvent
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener
import com.varunbarad.popularmovies.model.data.MovieList
import com.varunbarad.popularmovies.model.data.MovieStub
import com.varunbarad.popularmovies.screens.main.MainActivity
import com.varunbarad.popularmovies.util.MovieDbApi.MovieDbApiRetroFitHelper
import com.varunbarad.popularmovies.util.data.MovieDbHelper
import com.varunbarad.popularmovies.util.isConnectedToInternet
import com.varunbarad.popularmovies.util.readOneMovie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Creator: Varun Barad
 * Date: 2019-16-04
 * Project: PopularMovies
 */
private const val ACCEPTABLE_DELAY = 10 * 60 * 1000

class MoviesListFragment : Fragment(), ListItemClickListener {
    private var fragmentInteractionListener: OnFragmentInteractionListener? = null

    private lateinit var databaseHelper: MovieDbHelper

    private lateinit var dataBinding: FragmentMoviesListBinding
    private var moviesAdapter: MoviesAdapter? = null

    private var sortOrder: String = ""
    private var sortCriteriaEntries: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = this.arguments
        if (arguments != null) {
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

    override fun onDetach() {
        super.onDetach()
        this.fragmentInteractionListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.sortCriteriaEntries = this.context?.resources?.getStringArray(R.array.entries_sortCriteria) ?: emptyArray()

        this.dataBinding = FragmentMoviesListBinding.inflate(inflater, container, false)

        this.databaseHelper = MovieDbHelper(this.requireContext())

        this.dataBinding.spinnerSortCriteria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (this@MoviesListFragment.sortOrder != this@MoviesListFragment.sortCriteriaEntries[position]) {
                    this@MoviesListFragment.sortOrder = this@MoviesListFragment.sortCriteriaEntries[position]

                    when (this@MoviesListFragment.sortOrder.toLowerCase()) {
                        "most popular" -> {
                            if (this@MoviesListFragment.isPopularRefreshNeeded()) {
                                if (this@MoviesListFragment.context?.isConnectedToInternet() == true) {
                                    this@MoviesListFragment.fetchPopularMovies()
                                } else {
                                    this@MoviesListFragment.showNetworkError()
                                }
                            } else {
                                this@MoviesListFragment.showMovies(this@MoviesListFragment.retrievePopularMovies())
                            }
                        }
                        "highest rated" -> {
                            if (this@MoviesListFragment.isHighestRatedRefreshNeeded()) {
                                if (this@MoviesListFragment.context?.isConnectedToInternet() == true) {
                                    this@MoviesListFragment.fetchHighestRatedMovies()
                                } else {
                                    this@MoviesListFragment.showNetworkError()
                                }
                            } else {
                                this@MoviesListFragment.showMovies(this@MoviesListFragment.retrieveHighestRatedMovies())
                            }
                        }
                        "my favorites" -> {
                            this@MoviesListFragment.fetchFavoriteMovies()
                        }
                    }
                }
            }
        }

        this.dataBinding.recyclerViewMovies.setHasFixedSize(true)
        this.dataBinding.recyclerViewMovies.layoutManager = GridLayoutManager(
            this.context,
            2,
            RecyclerView.VERTICAL,
            false
        )

        this.sortOrder = this.sortCriteriaEntries[this.dataBinding.spinnerSortCriteria.selectedItemPosition]
        when (this.sortOrder.toLowerCase()) {
            "most popular" -> {
                if (this.isPopularRefreshNeeded()) {
                    if (this.context?.isConnectedToInternet() == true) {
                        this.fetchPopularMovies()
                    } else {
                        this.showNetworkError()
                    }
                } else {
                    this.showMovies(this.retrievePopularMovies())
                }
            }
            "highest rated" -> {
                if (this.isHighestRatedRefreshNeeded()) {
                    if (this.context?.isConnectedToInternet() == true) {
                        this.fetchHighestRatedMovies()
                    } else {
                        this.showNetworkError()
                    }
                } else {
                    this.showMovies(this.retrieveHighestRatedMovies())
                }
            }
        }

        return this.dataBinding.root
    }

    override fun onResume() {
        super.onResume()

        val activity = this.activity
        if (activity is MainActivity) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if (this.dataBinding.spinnerSortCriteria.selectedItem.toString().equals("my favories", true)) {
            val cursor = this.databaseHelper.queryFavoriteMovies()
            if (cursor != null) {
                if (cursor.count > 1) {
                    this.showNoFavorites()
                }
                cursor.close()
            }
        }
    }

    override fun onDestroy() {
        this.databaseHelper.close()
        super.onDestroy()
    }

    override fun onItemClick(position: Int) {
        val movie = this.moviesAdapter?.movies?.get(position)
        if (movie != null) {
            this.fragmentInteractionListener
                ?.onFragmentInteraction(FragmentInteractionEvent.OpenMovieDetailsEvent(movie))
        }
    }

    private fun fetchPopularMovies() {
        this.showProgress()

        val retrofit = Retrofit.Builder()
            .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper::class.java)

        movieDbApiRetroFitHelper
            .getPopularMovies(1)
            .enqueue(object : Callback<MovieList?> {
                override fun onFailure(call: Call<MovieList?>, t: Throwable) {
                    this@MoviesListFragment.showNetworkError()
                }

                override fun onResponse(call: Call<MovieList?>, response: Response<MovieList?>) {
                    val movies = response.body()?.results
                    if (movies != null) {
                        this@MoviesListFragment.showMovies(movies)
                        this@MoviesListFragment.storePopularMovies(movies)
                    } else {
                        this@MoviesListFragment.showNetworkError()
                    }
                }
            })
    }

    private fun fetchHighestRatedMovies() {
        this.showProgress()

        val retrofit = Retrofit.Builder()
            .baseUrl(MovieDbApiRetroFitHelper.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val movieDbApiRetroFitHelper = retrofit.create(MovieDbApiRetroFitHelper::class.java)

        movieDbApiRetroFitHelper
            .getHighestRatedMovies(1)
            .enqueue(object : Callback<MovieList?> {
                override fun onFailure(call: Call<MovieList?>, t: Throwable) {
                    this@MoviesListFragment.showNetworkError()
                }

                override fun onResponse(call: Call<MovieList?>, response: Response<MovieList?>) {
                    val movies = response.body()?.results
                    if (movies != null) {
                        this@MoviesListFragment.showMovies(movies)
                        this@MoviesListFragment.storeHighestRatedMovies(movies)
                    } else {
                        this@MoviesListFragment.showNetworkError()
                    }
                }
            })
    }

    private fun fetchFavoriteMovies() {
        val cursor = this.databaseHelper.queryFavoriteMovies()
        if (cursor != null) {
            if (cursor.count > 0) {
                val favoriteMovies = mutableListOf<MovieStub>()

                cursor.moveToFirst()
                do {
                    favoriteMovies.add(cursor.readOneMovie().toMovieStub())
                } while (cursor.moveToNext())

                this.showMovies(favoriteMovies)
            } else {
                this.showNoFavorites()
            }

            cursor.close()
        } else {
            this.showNoFavorites()
        }
    }

    private fun showNetworkError() {
        this.dataBinding.placeholderProgress
            .visibility = View.GONE
        this.dataBinding.recyclerViewMovies
            .visibility = View.GONE
        this.dataBinding.placeHolderError
            .visibility = View.VISIBLE
        this.dataBinding.placeHolderNoFavorites
            .visibility = View.GONE
    }

    private fun showProgress() {
        this.dataBinding.placeholderProgress
            .visibility = View.VISIBLE
        this.dataBinding.recyclerViewMovies
            .visibility = View.GONE
        this.dataBinding.placeHolderError
            .visibility = View.GONE
        this.dataBinding.placeHolderNoFavorites
            .visibility = View.GONE
    }

    private fun showNoFavorites() {
        this.dataBinding.placeholderProgress
            .visibility = View.GONE
        this.dataBinding.recyclerViewMovies
            .visibility = View.GONE
        this.dataBinding.placeHolderError
            .visibility = View.GONE
        this.dataBinding.placeHolderNoFavorites
            .visibility = View.VISIBLE
    }

    private fun showMovies(movies: List<MovieStub>) {
        this.moviesAdapter = MoviesAdapter(movies, this)
        this.dataBinding.recyclerViewMovies.adapter = this.moviesAdapter

        this.dataBinding.placeholderProgress
            .visibility = View.GONE
        this.dataBinding.recyclerViewMovies
            .visibility = View.VISIBLE
        this.dataBinding.placeHolderError
            .visibility = View.GONE
        this.dataBinding.placeHolderNoFavorites
            .visibility = View.GONE
    }

    fun addFavorite(movieStub: MovieStub) {
        if (this.sortOrder.toLowerCase() == "my favorites") {
            if (this.dataBinding.recyclerViewMovies.visibility == View.VISIBLE) {
                this.moviesAdapter?.addMovie(movieStub)
            } else {
                this.fetchFavoriteMovies()
            }
        }
    }

    fun removeFavorite(movieId: Long) {
        if (this.sortOrder.toLowerCase() == "my favorites") {
            this.moviesAdapter?.removeMovie(movieId)
            if ((this.moviesAdapter?.itemCount ?: 0) < 1) {
                this.showNoFavorites()
            }
        }
    }

    private fun isPopularRefreshNeeded(): Boolean {
        val preferences = this.context?.getSharedPreferences(
            this.context?.getString(R.string.PREFS_NAME),
            Context.MODE_PRIVATE
        )

        val lastLoadTime = preferences?.getLong(
            this.context?.getString(R.string.PREFS_KEY_POPULAR_TIMESTAMP),
            0
        ) ?: 0
        return ((System.currentTimeMillis() - lastLoadTime) > ACCEPTABLE_DELAY)
    }

    private fun isHighestRatedRefreshNeeded(): Boolean {
        val preferences = this.context?.getSharedPreferences(
            this.context?.getString(R.string.PREFS_NAME),
            Context.MODE_PRIVATE
        )

        val lastLoadTime = preferences?.getLong(
            this.context?.getString(R.string.PREFS_KEY_HIGHEST_RATED_TIMESTAMP),
            0
        ) ?: 0
        return ((System.currentTimeMillis() - lastLoadTime) > ACCEPTABLE_DELAY)
    }

    private fun storePopularMovies(movies: List<MovieStub>) {
        val preferences = this.context?.getSharedPreferences(
            this.context?.getString(R.string.PREFS_NAME),
            Context.MODE_PRIVATE
        )
        val editor = preferences?.edit()

        if (editor != null) {
            editor.putString(
                this.context?.getString(R.string.PREFS_KEY_POPULAR),
                Moshi.Builder().build().adapter<List<MovieStub>>(
                    Types.newParameterizedType(
                        List::class.java,
                        MovieStub::class.java
                    )
                ).toJson(movies)
            )
            editor.putLong(
                this.context?.getString(R.string.PREFS_KEY_POPULAR_TIMESTAMP),
                System.currentTimeMillis()
            )
            editor.apply()
        }
    }

    private fun storeHighestRatedMovies(movies: List<MovieStub>) {
        val preferences = this.context?.getSharedPreferences(
            this.context?.getString(R.string.PREFS_NAME),
            Context.MODE_PRIVATE
        )
        val editor = preferences?.edit()

        if (editor != null) {
            editor.putString(
                this.context?.getString(R.string.PREFS_KEY_HIGHEST_RATED),
                Moshi.Builder().build().adapter<List<MovieStub>>(
                    Types.newParameterizedType(
                        List::class.java,
                        MovieStub::class.java
                    )
                ).toJson(movies)
            )
            editor.putLong(
                this.context?.getString(R.string.PREFS_KEY_HIGHEST_RATED_TIMESTAMP),
                System.currentTimeMillis()
            )
            editor.apply()
        }
    }

    private fun retrievePopularMovies(): List<MovieStub> {
        val preferences = this.context?.getSharedPreferences(
            this.context?.getString(R.string.PREFS_NAME),
            Context.MODE_PRIVATE
        )

        return Moshi.Builder().build().adapter<List<MovieStub>>(
            Types.newParameterizedType(
                List::class.java,
                MovieStub::class.java
            )
        ).fromJson(
            preferences?.getString(this.context?.getString(R.string.PREFS_KEY_POPULAR), "[]") ?: "[]"
        ) ?: emptyList()
    }

    private fun retrieveHighestRatedMovies(): List<MovieStub> {
        val preferences = this.context?.getSharedPreferences(
            this.context?.getString(R.string.PREFS_NAME),
            Context.MODE_PRIVATE
        )

        return Moshi.Builder().build().adapter<List<MovieStub>>(
            Types.newParameterizedType(
                List::class.java,
                MovieStub::class.java
            )
        ).fromJson(
            preferences?.getString(this.context?.getString(R.string.PREFS_KEY_HIGHEST_RATED), "[]") ?: "[]"
        ) ?: emptyList()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MoviesListFragment.
         */
        @JvmStatic
        fun newInstance(): MoviesListFragment {
            return MoviesListFragment().apply {
                arguments = Bundle()
            }
        }
    }
}
