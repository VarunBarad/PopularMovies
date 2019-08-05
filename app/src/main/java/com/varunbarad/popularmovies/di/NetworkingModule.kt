package com.varunbarad.popularmovies.di

import android.content.Context
import com.varunbarad.popularmovies.external_services.movie_db_api.Constants
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

object NetworkingModule {
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofitInstance: Retrofit

    fun provideOkHttpClient(context: Context): OkHttpClient {
        if (!this::okHttpClient.isInitialized) {
            this.okHttpClient = OkHttpClient.Builder()
                .cache(Cache(File(context.applicationContext.cacheDir, ""), (5 * 1024 * 1024))) // Create 5 MB cache
                .build()
        }

        return this.okHttpClient
    }

    fun provideRetrofitInstance(context: Context): Retrofit {
        if (!this::retrofitInstance.isInitialized) {
            this.retrofitInstance = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(this.provideOkHttpClient(context))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        return this.retrofitInstance
    }
}
