package com.varunbarad.popularmovies.eventlistener

import com.varunbarad.popularmovies.model.MovieStub

/**
 * Creator: Varun Barad
 * Date: 2019-06-04
 * Project: PopularMovies
 */
sealed class FragmentInteractionEvent {
    class OpenMovieDetailsEvent(val movie: MovieStub): FragmentInteractionEvent()
    class AddToFavoriteEvent(val movie: MovieStub): FragmentInteractionEvent()
    class RemoveFromFavoriteEvent(val movieId: Long): FragmentInteractionEvent()
}
