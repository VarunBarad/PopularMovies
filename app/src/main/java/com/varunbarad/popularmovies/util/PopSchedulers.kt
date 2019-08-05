package com.varunbarad.popularmovies.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object PopSchedulers {
    private var schedulers: RxSchedulers = RxSchedulers.Default

    fun enableTesting() {
        this.schedulers = RxSchedulers.Test
    }

    fun disableTesting() {
        this.schedulers = RxSchedulers.Default
    }

    fun io(): Scheduler = this.schedulers.io()
    fun computation(): Scheduler = this.schedulers.computation()
    fun main(): Scheduler = this.schedulers.main()

    private sealed class RxSchedulers {
        abstract fun io(): Scheduler
        abstract fun computation(): Scheduler
        abstract fun main(): Scheduler

        object Test : RxSchedulers() {
            override fun io(): Scheduler = Schedulers.trampoline()
            override fun computation(): Scheduler = Schedulers.trampoline()
            override fun main(): Scheduler = Schedulers.trampoline()
        }

        object Default : RxSchedulers() {
            override fun io(): Scheduler = Schedulers.io()
            override fun computation(): Scheduler = Schedulers.computation()
            override fun main(): Scheduler = AndroidSchedulers.mainThread()
        }
    }
}
