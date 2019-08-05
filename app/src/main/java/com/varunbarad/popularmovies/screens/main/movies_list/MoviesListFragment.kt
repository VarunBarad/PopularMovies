package com.varunbarad.popularmovies.screens.main.movies_list

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.varunbarad.popularmovies.di.NetworkingModule
import com.varunbarad.popularmovies.di.external_services.LocalDatabaseModule
import com.varunbarad.popularmovies.eventlistener.FragmentInteractionEvent
import com.varunbarad.popularmovies.eventlistener.ListItemClickListener
import com.varunbarad.popularmovies.eventlistener.OnFragmentInteractionListener
import com.varunbarad.popularmovies.external_services.local_database.movie_details.MovieDetailsDao
import com.varunbarad.popularmovies.external_services.movie_db_api.MovieDbApiService
import com.varunbarad.popularmovies.model.MovieStub
import com.varunbarad.popularmovies.model.toMovieList
import com.varunbarad.popularmovies.screens.main.MainActivity
import com.varunbarad.popularmovies.util.isConnectedToInternet
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Creator: Varun Barad
 * Date: 2019-16-04
 * Project: PopularMovies
 */
private const val ACCEPTABLE_DELAY = 10 * 60 * 1000

class MoviesListFragment : Fragment(), ListItemClickListener {
    private var fragmentInteractionListener: OnFragmentInteractionListener? = null
    private val disposable: CompositeDisposable = CompositeDisposable()

    private val moviesDao: MovieDetailsDao by lazy {
        LocalDatabaseModule(this.requireActivity().application).provideMoviesDao()
    }

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

        if (this.dataBinding.spinnerSortCriteria.selectedItem.toString().equals("my favorites", true)) {
            this.disposable.add(
                Single.fromCallable { this.moviesDao.getFavoriteMovies() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onError = {
                            Log.w(this.requireContext().packageName, it.message)
                            this.showNoFavorites()
                        },
                        onSuccess = {
                            if (it.isEmpty()) {
                                this.showNoFavorites()
                            }
                        }
                    )
            )
        }
    }

    override fun onDestroy() {
        this.disposable.clear()
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

        val retrofit = NetworkingModule.provideRetrofitInstance(this.requireContext())

        val movieDbApiRetroFitHelper = retrofit.create(MovieDbApiService::class.java)

        this.disposable.add(
            movieDbApiRetroFitHelper.getPopularMovies(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { this.showNetworkError() },
                    onSuccess = {
                        val movies = it?.toMovieList()?.results
                        if (movies != null) {
                            this.showMovies(movies)
                            this.storePopularMovies(movies)
                        } else {
                            this.showNetworkError()
                        }
                    }
                )
        )
    }

    private fun fetchHighestRatedMovies() {
        this.showProgress()

        val retrofit = NetworkingModule.provideRetrofitInstance(this.requireContext())

        val movieDbApiRetroFitHelper = retrofit.create(MovieDbApiService::class.java)

        this.disposable.add(
            movieDbApiRetroFitHelper.getHighestRatedMovies(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { this.showNetworkError() },
                    onSuccess = {
                        val movies = it?.toMovieList()?.results
                        if (movies != null) {
                            this.showMovies(movies)
                            this.storeHighestRatedMovies(movies)
                        } else {
                            this.showNetworkError()
                        }
                    }
                )
        )
    }

    private fun fetchFavoriteMovies() {
        this.disposable.add(
            Single.fromCallable { this.moviesDao.getFavoriteMovies() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = {
                        Log.w(this.requireContext().packageName, it.message)
                        this.showNoFavorites()
                    },
                    onSuccess = { movies ->
                        if (movies.isNotEmpty()) {
                            this.showMovies(movies.map { it.toMovieDetails().toMovieStub() })
                        } else {
                            this.showNoFavorites()
                        }
                    }
                )
        )
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
