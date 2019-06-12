package com.varunbarad.popularmovies.model.data

import android.content.ContentValues
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.varunbarad.popularmovies.util.data.MovieContract
import java.io.IOException

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
@JsonClass(generateAdapter = true)
data class MovieDetails(
    @Json(name = "adult") val isAdult: Boolean = false,
    @Json(name = "backdrop_path") val backdropPath: String = "",
    @Json(name = "belongs_to_collection") val collection: CollectionStub? = null,
    @Json(name = "budget") val budget: Long,
    @Json(name = "genres") val genres: List<Genre> = emptyList(),
    @Json(name = "homepage") val homepage: String = "",
    @Json(name = "id") val id: Long,
    @Json(name = "imdb_id") val idIMDB: String,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_title") val originalTitle: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "popularity") val popularity: Double,
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "production_companies") val productionCompanies: List<CompanyStub>,
    @Json(name = "production_countries") val productionCountries: List<Country>,
    @Json(name = "release_date") val releaseDate: String = "",
    @Json(name = "revenue") val revenue: Long,
    @Json(name = "runtime") val runtime: Int = 0,
    @Json(name = "spoken_languages") val spokenLanguages: List<Language>,
    @Json(name = "status") val status: String = "",
    @Json(name = "tagline") val tagLine: String,
    @Json(name = "title") val title: String,
    @Json(name = "video") val isVideo: Boolean = false,
    @Json(name = "vote_average") val averageVote: Double,
    @Json(name = "vote_count") val numberOfVotes: Long,
    @Json(name = "videos") val videos: VideoList? = null,
    @Json(name = "images") val images: ImageList? = null,
    @Json(name = "reviews") val reviews: ReviewList? = null,
    @Json(name = "recommendations") val recommendations: MovieList? = null,
    @Json(name = "similar_movies") val similarMovies: MovieList? = null
) {
    fun toContentValues(isFavorite: Boolean): ContentValues {
        val moshi = Moshi.Builder().build()

        return ContentValues().apply {
            put(MovieContract.Movie.COLUMN_ADULT, if (isAdult) 1 else 0)
            put(MovieContract.Movie.COLUMN_BACKDROP_PATH, backdropPath)
            put(MovieContract.Movie.COLUMN_COLLECTION, moshi.adapter(CollectionStub::class.java).toJson(collection))
            put(MovieContract.Movie.COLUMN_BUDGET, budget)
            put(
                MovieContract.Movie.COLUMN_GENRES,
                moshi.adapter<List<Genre>>(Types.newParameterizedType(List::class.java, Genre::class.java)).toJson(
                    genres
                )
            )
            put(MovieContract.Movie.COLUMN_HOMEPAGE, homepage)
            put(MovieContract.Movie.COLUMN_MOVIE_ID, id)
            put(MovieContract.Movie.COLUMN_IMDB_ID, idIMDB)
            put(MovieContract.Movie.COLUMN_ORIGINAL_LANGUAGE, originalLanguage)
            put(MovieContract.Movie.COLUMN_ORIGINAL_TITLE, originalTitle)
            put(MovieContract.Movie.COLUMN_OVERVIEW, overview)
            put(MovieContract.Movie.COLUMN_POPULARITY, popularity)
            put(MovieContract.Movie.COLUMN_POSTER_PATH, posterPath)
            put(
                MovieContract.Movie.COLUMN_PRODUCTION_COMPANIES,
                moshi.adapter<List<CompanyStub>>(
                    Types.newParameterizedType(
                        List::class.java,
                        CompanyStub::class.java
                    )
                ).toJson(productionCompanies)
            )
            put(
                MovieContract.Movie.COLUMN_PRODUCTION_COUNTRIES,
                moshi.adapter<List<Country>>(Types.newParameterizedType(List::class.java, Country::class.java)).toJson(
                    productionCountries
                )
            )
            put(MovieContract.Movie.COLUMN_RELEASE_DATE, releaseDate)
            put(MovieContract.Movie.COLUMN_REVENUE, revenue)
            put(MovieContract.Movie.COLUMN_RUNTIME, runtime)
            put(
                MovieContract.Movie.COLUMN_SPOKEN_LANGUAGES,
                moshi.adapter<List<Language>>(
                    Types.newParameterizedType(
                        List::class.java,
                        Language::class.java
                    )
                ).toJson(spokenLanguages)
            )
            put(MovieContract.Movie.COLUMN_STATUS, status)
            put(MovieContract.Movie.COLUMN_TAGLINE, tagLine)
            put(MovieContract.Movie.COLUMN_TITLE, title)
            put(MovieContract.Movie.COLUMN_IS_VIDEO, if (isVideo) 1 else 0)
            put(MovieContract.Movie.COLUMN_VOTE_AVERAGE, averageVote)
            put(MovieContract.Movie.COLUMN_VOTE_COUNT, numberOfVotes)
            put(MovieContract.Movie.COLUMN_VIDEOS, moshi.adapter(VideoList::class.java).toJson(videos))
            put(MovieContract.Movie.COLUMN_IMAGES, moshi.adapter(ImageList::class.java).toJson(images))
            put(MovieContract.Movie.COLUMN_REVIEWS, moshi.adapter(ReviewList::class.java).toJson(reviews))
            put(
                MovieContract.Movie.COLUMN_RECOMMENDED_MOVIES,
                moshi.adapter(MovieList::class.java).toJson(recommendations)
            )
            put(MovieContract.Movie.COLUMN_SIMILAR_MOVIES, moshi.adapter(MovieList::class.java).toJson(similarMovies))
            put(MovieContract.Movie.COLUMN_IS_FAVORITE, if (isFavorite) 1 else 0)
        }
    }

    @Throws(IOException::class)
    fun toMovieStub(): MovieStub {
        return Moshi.Builder().build().adapter(MovieStub::class.java).fromJson(this.toString())!!
    }

    override fun toString(): String {
        return Moshi.Builder().build().adapter(MovieDetails::class.java).toJson(this)
    }

    companion object {
        @JvmStatic
        fun initFromContentValues(values: ContentValues): MovieDetails {
            val moshi = Moshi.Builder().build()

            return MovieDetails(
                isAdult = (values.getAsInteger(MovieContract.Movie.COLUMN_ADULT) == 1),
                backdropPath = values.getAsString(MovieContract.Movie.COLUMN_BACKDROP_PATH),
                collection = moshi.adapter(CollectionStub::class.java).fromJson(values.getAsString(MovieContract.Movie.COLUMN_COLLECTION)),
                budget = values.getAsLong(MovieContract.Movie.COLUMN_BUDGET),
                genres = moshi.adapter<List<Genre>>(
                    Types.newParameterizedType(
                        List::class.java,
                        Genre::class.java
                    )
                ).fromJson(values.getAsString(MovieContract.Movie.COLUMN_GENRES)) ?: emptyList(),
                homepage = values.getAsString(MovieContract.Movie.COLUMN_HOMEPAGE),
                id = values.getAsLong(MovieContract.Movie.COLUMN_MOVIE_ID),
                idIMDB = values.getAsString(MovieContract.Movie.COLUMN_IMDB_ID),
                originalLanguage = values.getAsString(MovieContract.Movie.COLUMN_ORIGINAL_LANGUAGE),
                originalTitle = values.getAsString(MovieContract.Movie.COLUMN_ORIGINAL_TITLE),
                overview = values.getAsString(MovieContract.Movie.COLUMN_OVERVIEW),
                popularity = values.getAsDouble(MovieContract.Movie.COLUMN_POPULARITY),
                posterPath = values.getAsString(MovieContract.Movie.COLUMN_POSTER_PATH),
                productionCompanies = moshi.adapter<List<CompanyStub>>(
                    Types.newParameterizedType(
                        List::class.java,
                        CompanyStub::class.java
                    )
                ).fromJson(values.getAsString(MovieContract.Movie.COLUMN_PRODUCTION_COMPANIES)) ?: emptyList(),
                productionCountries = moshi.adapter<List<Country>>(
                    Types.newParameterizedType(
                        List::class.java,
                        Country::class.java
                    )
                ).fromJson(values.getAsString(MovieContract.Movie.COLUMN_PRODUCTION_COMPANIES)) ?: emptyList(),
                releaseDate = values.getAsString(MovieContract.Movie.COLUMN_RELEASE_DATE),
                revenue = values.getAsLong(MovieContract.Movie.COLUMN_REVENUE),
                runtime = values.getAsInteger(MovieContract.Movie.COLUMN_RUNTIME),
                spokenLanguages = moshi.adapter<List<Language>>(
                    Types.newParameterizedType(
                        List::class.java,
                        Language::class.java
                    )
                ).fromJson(values.getAsString(MovieContract.Movie.COLUMN_SPOKEN_LANGUAGES)) ?: emptyList(),
                status = values.getAsString(MovieContract.Movie.COLUMN_STATUS),
                tagLine = values.getAsString(MovieContract.Movie.COLUMN_TAGLINE),
                title = values.getAsString(MovieContract.Movie.COLUMN_TITLE),
                isVideo = (values.getAsInteger(MovieContract.Movie.COLUMN_IS_VIDEO) == 1),
                averageVote = values.getAsDouble(MovieContract.Movie.COLUMN_VOTE_AVERAGE),
                numberOfVotes = values.getAsLong(MovieContract.Movie.COLUMN_VOTE_COUNT),
                videos = moshi.adapter(VideoList::class.java).fromJson(values.getAsString(MovieContract.Movie.COLUMN_VIDEOS)),
                images = moshi.adapter(ImageList::class.java).fromJson(values.getAsString(MovieContract.Movie.COLUMN_IMAGES)),
                reviews = moshi.adapter(ReviewList::class.java).fromJson(values.getAsString(MovieContract.Movie.COLUMN_REVIEWS)),
                recommendations = moshi.adapter(MovieList::class.java).fromJson(values.getAsString(MovieContract.Movie.COLUMN_RECOMMENDED_MOVIES)),
                similarMovies = moshi.adapter(MovieList::class.java).fromJson(values.getAsString(MovieContract.Movie.COLUMN_SIMILAR_MOVIES))
            )
        }
    }
}
