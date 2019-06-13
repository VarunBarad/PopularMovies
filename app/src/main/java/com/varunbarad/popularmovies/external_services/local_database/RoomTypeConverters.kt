package com.varunbarad.popularmovies.external_services.local_database

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.varunbarad.popularmovies.model.data.*

/**
 * Creator: Varun Barad
 * Date: 2019-06-12
 * Project: PopularMovies
 */
class RoomTypeConverters {
    companion object {
        private val moshi: Moshi = Moshi.Builder().build()

        @JvmStatic
        @TypeConverter
        fun fromGenresList(value: List<Genre>): String {
            return moshi.adapter<List<Genre>>(Types.newParameterizedType(List::class.java, Genre::class.java))
                .toJson(value)
        }

        @JvmStatic
        @TypeConverter
        fun toGenresList(value: String): List<Genre> {
            return moshi.adapter<List<Genre>>(Types.newParameterizedType(List::class.java, Genre::class.java)).fromJson(
                value
            ) ?: emptyList()
        }

        @JvmStatic
        @TypeConverter
        fun fromVideoList(value: VideoList?): String? {
            return if (value != null) {
                moshi.adapter(VideoList::class.java).toJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toVideoList(value: String?): VideoList? {
            return if (value != null) {
                moshi.adapter(VideoList::class.java).fromJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun fromImageList(value: ImageList?): String? {
            return if (value != null) {
                moshi.adapter(ImageList::class.java).toJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toImageList(value: String?): ImageList? {
            return if (value != null) {
                moshi.adapter(ImageList::class.java).fromJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun fromReviewList(value: ReviewList?): String? {
            return if (value != null) {
                moshi.adapter(ReviewList::class.java).toJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toReviewList(value: String?): ReviewList? {
            return if (value != null) {
                moshi.adapter(ReviewList::class.java).fromJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun fromMovieList(value: MovieList?): String? {
            return if (value != null) {
                moshi.adapter(MovieList::class.java).toJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toMovieList(value: String?): MovieList? {
            return if (value != null) {
                moshi.adapter(MovieList::class.java).fromJson(value)
            } else {
                null
            }
        }
    }
}
