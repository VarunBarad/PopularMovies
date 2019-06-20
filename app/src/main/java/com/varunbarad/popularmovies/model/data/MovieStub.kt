package com.varunbarad.popularmovies.model.data

import android.os.Parcel
import android.os.Parcelable
import com.varunbarad.popularmovies.external_services.movie_db_api.models.ApiMovieStub

/**
 * Creator: Varun Barad
 * Date: 2019-06-05
 * Project: PopularMovies
 */
data class MovieStub(
    val posterPath: String,
    val adult: Boolean = false,
    val overview: String = "",
    val releaseDate: String? = null,
    val id: Long,
    val title: String,
    val backdropPath: String = "",
    val voteAverage: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        (parcel.readString() ?: ""),
        (parcel.readByte() != 0.toByte()),
        (parcel.readString() ?: ""),
        parcel.readString(),
        parcel.readLong(),
        (parcel.readString() ?: ""),
        (parcel.readString() ?: ""),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(posterPath)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(backdropPath)
        parcel.writeDouble(voteAverage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieStub> {
        override fun createFromParcel(parcel: Parcel): MovieStub {
            return MovieStub(parcel)
        }

        override fun newArray(size: Int): Array<MovieStub?> {
            return arrayOfNulls(size)
        }
    }
}

fun ApiMovieStub.toMovieStub(): MovieStub {
    return MovieStub(
        posterPath = this.posterPath,
        adult = this.adult,
        overview = this.overview,
        releaseDate = this.releaseDate,
        id = this.id,
        title = this.title,
        backdropPath = this.backdropPath,
        voteAverage = this.voteAverage
    )
}
