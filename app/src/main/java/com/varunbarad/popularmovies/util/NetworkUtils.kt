@file:JvmName("NetworkUtils")

package com.varunbarad.popularmovies.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Creator: Varun Barad
 * Date: 2019-06-04
 * Project: PopularMovies
 */
fun Context.isConnectedToInternet(): Boolean {
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnected ?: false
}
