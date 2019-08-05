package com.varunbarad.popularmovies.screens.main

import androidx.lifecycle.ViewModel
import com.varunbarad.popularmovies.eventlistener.FragmentInteractionEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class MainActivityViewModel : ViewModel() {
    val fragmentInteractionEvent: Subject<FragmentInteractionEvent> = BehaviorSubject.create()
}
