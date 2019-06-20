package com.varunbarad.popularmovies.external_services.local_database

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.varunbarad.popularmovies.external_services.local_database.models.*

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
        fun fromGenresList(value: List<GenreDb>): String {
            return moshi.adapter<List<GenreDb>>(Types.newParameterizedType(List::class.java, GenreDb::class.java))
                .toJson(value)
        }

        @JvmStatic
        @TypeConverter
        fun toGenresList(value: String): List<GenreDb> {
            return moshi.adapter<List<GenreDb>>(
                Types.newParameterizedType(
                    List::class.java,
                    GenreDb::class.java
                )
            ).fromJson(
                value
            ) ?: emptyList()
        }

        @JvmStatic
        @TypeConverter
        fun fromVideoList(value: VideoListDb?): String? {
            return if (value != null) {
                moshi.adapter(VideoListDb::class.java).toJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toVideoList(value: String?): VideoListDb? {
            return if (value != null) {
                moshi.adapter(VideoListDb::class.java).fromJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun fromImageList(value: ImageListDb?): String? {
            return if (value != null) {
                moshi.adapter(ImageListDb::class.java).toJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toImageList(value: String?): ImageListDb? {
            return if (value != null) {
                moshi.adapter(ImageListDb::class.java).fromJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun fromReviewList(value: ReviewListDb?): String? {
            return if (value != null) {
                moshi.adapter(ReviewListDb::class.java).toJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toReviewList(value: String?): ReviewListDb? {
            return if (value != null) {
                moshi.adapter(ReviewListDb::class.java).fromJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun fromMovieList(value: MovieListDb?): String? {
            return if (value != null) {
                moshi.adapter(MovieListDb::class.java).toJson(value)
            } else {
                null
            }
        }

        @JvmStatic
        @TypeConverter
        fun toMovieList(value: String?): MovieListDb? {
            return if (value != null) {
                moshi.adapter(MovieListDb::class.java).fromJson(value)
            } else {
                null
            }
        }
    }
}
