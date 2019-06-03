@file:JvmName("MovieDbApiImageHelper")
package com.varunbarad.popularmovies.util.MovieDbApi

/**
 * Creator: vbarad
 * Date: 2019-06-03
 * Project: PopularMovies
 */

private const val baseUrlImage = "https://image.tmdb.org/t/p/"

/**
 * Get the URL(for specific width) for image given it's ID
 *
 * @param imageId ID of the image whose URL is needed
 * @param widthPx The width(in px) of the view which is to be used to display the image
 * @return The URL of the image(of appropriate width) whose ID was passed
 */
fun getImageUrl(imageId: String, widthPx: Int): String {
    val imageSize: String = when {
        widthPx > 780 -> "original"
        widthPx > 500 -> "w780"
        widthPx > 342 -> "w500"
        widthPx > 185 -> "w342"
        widthPx > 154 -> "w185"
        widthPx > 92 -> "w154"
        widthPx > 0 -> "w92"
        else -> "w185"     // Default Value
    }

    return "$baseUrlImage$imageSize/$imageId"
}

/**
 * Get the URL(for original size) for image given it's ID
 *
 * @param imageId ID of the image whose URL is needed
 * @return The URL of the image(of appropriate width) whose ID was passed
 */
fun getOriginalImageUrl(imageId: String): String {
    return "${baseUrlImage}original/$imageId"
}
