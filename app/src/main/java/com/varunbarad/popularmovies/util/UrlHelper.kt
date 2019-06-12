@file:JvmName("UrlHelper")

package com.varunbarad.popularmovies.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openUrlInBrowser(url: String) {
    val websiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (websiteIntent.resolveActivity(this.packageManager) != null) {
        this.startActivity(websiteIntent)
    }
}

fun Context.openYouTubeVideo(videoUrl: String) {
    val videoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
    if (videoIntent.resolveActivity(this.packageManager) != null) {
        this.startActivity(videoIntent)
    }
}
