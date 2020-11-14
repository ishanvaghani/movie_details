package com.example.moviedetails.ViewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedetails.Model.*
import com.example.moviedetails.Repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private var movieDetails: MutableLiveData<MovieDetails> = MutableLiveData()
    private var popularMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var nowPlayingMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var topRatedMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var upcomingMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var similarMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    private var movieVideos: MutableLiveData<List<Video>> = MutableLiveData()

    fun readyMovieDetails(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getMovieDetails(movieId)
            movieDetails.value = movieRepository.movieDetails
        }
    }

    fun readyPopularMovies() {
        viewModelScope.launch {
            movieRepository.getPopularMovies()
            popularMovies.value = movieRepository.popularMovies
        }
    }

    fun readyNowPlayingMovies() {
        viewModelScope.launch {
            movieRepository.getNowPlayingMovies()
            nowPlayingMovies.value = movieRepository.nowPlayingMovies
        }
    }

    fun readyTopRatedMovies() {
        viewModelScope.launch {
            movieRepository.getTopRatedMovies()
            topRatedMovies.value = movieRepository.topRatedMovies
        }
    }

    fun readyUpcomingMovies() {
        viewModelScope.launch {
            movieRepository.getUpcomingMovies()
            upcomingMovies.value = movieRepository.upcomingMovies
        }
    }

    fun readySimilarMovies(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getSimilarMovies(movieId)
            similarMovies.value = movieRepository.similarMovies
        }
    }

    fun readyMovieVideos(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getMovieVideos(movieId)
            movieVideos.value = movieRepository.movieVideos
        }
    }

    fun getMovieDetails(): LiveData<MovieDetails> = movieDetails
    fun getPopularMovie(): LiveData<List<Movie>> = popularMovies
    fun getNowPlayingMovies(): LiveData<List<Movie>> = nowPlayingMovies
    fun getTopRatedMovies(): LiveData<List<Movie>> = topRatedMovies
    fun getUpcomingMovies(): LiveData<List<Movie>> = upcomingMovies
    fun getSimilarMovies(): LiveData<List<Movie>> = similarMovies
    fun getMovieVideos(): LiveData<List<Video>> = movieVideos
}