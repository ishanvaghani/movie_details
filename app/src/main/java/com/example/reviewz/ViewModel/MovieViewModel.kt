package com.example.reviewz.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.reviewz.Model.*
import com.example.reviewz.Repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private var movieDetails: MutableLiveData<MovieDetails> = MutableLiveData()
    private var nowPlayingMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var movieVideos: MutableLiveData<List<Video>> = MutableLiveData()

    fun readyMovieDetails(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getMovieDetails(movieId)
            movieDetails.value = movieRepository.movieDetails
        }
    }

    fun readyNowPlayingMovies() {
        viewModelScope.launch {
            movieRepository.getNowPlayingMovies()
            nowPlayingMovies.value = movieRepository.nowPlayingMovies
        }
    }

    fun readyMovieVideos(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getMovieVideos(movieId)
            movieVideos.value = movieRepository.movieVideos
        }
    }

    val popularMovies = movieRepository.getPopularMovies().cachedIn(viewModelScope)
    val topRatedMovies = movieRepository.getTopRatedMovies().cachedIn(viewModelScope)
    val upcomingMovies = movieRepository.getUpcomingMovies().cachedIn(viewModelScope)
    fun similarMovies(movieId: Int) =
        movieRepository.getSimilarMovies(movieId).cachedIn(viewModelScope)
    fun getGenreMovies(genreId: Int) = movieRepository.getGenreMovies(genreId).cachedIn(viewModelScope)

    fun getMovieDetails(): LiveData<MovieDetails> = movieDetails
    fun getNowPlayingMovies(): LiveData<List<Movie>> = nowPlayingMovies
    fun getMovieVideos(): LiveData<List<Video>> = movieVideos
}