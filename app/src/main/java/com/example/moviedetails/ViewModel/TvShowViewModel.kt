package com.example.moviedetails.ViewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedetails.Model.Guest
import com.example.moviedetails.Model.Movie
import com.example.moviedetails.Model.TvShowDetails
import com.example.moviedetails.Model.Video
import com.example.moviedetails.Repository.TvShowRepository
import kotlinx.coroutines.launch

class TvShowViewModel @ViewModelInject constructor(private val tvShowRepository: TvShowRepository): ViewModel() {

    private var tvAiringToday: MutableLiveData<List<Movie>> = MutableLiveData()
    private var tvPopular: MutableLiveData<List<Movie>> = MutableLiveData()
    private var tvTopRated: MutableLiveData<List<Movie>> = MutableLiveData()
    private var tvOnTheAir: MutableLiveData<List<Movie>> = MutableLiveData()
    private var similarTvShows: MutableLiveData<List<Movie>> = MutableLiveData()
    private var tvShowVideos: MutableLiveData<List<Video>> = MutableLiveData()
    private var tvShowDetails: MutableLiveData<TvShowDetails> = MutableLiveData()
    private var search: MutableLiveData<List<Movie>> = MutableLiveData()

    fun readyTvAiringToday() {
        viewModelScope.launch {
            tvShowRepository.getTvAiringToday()
            tvAiringToday.value = tvShowRepository.tvAiringToday
        }
    }

    fun readyTvPopular() {
        viewModelScope.launch {
            tvShowRepository.getTvPopular()
            tvPopular.value = tvShowRepository.tvPopular
        }
    }

    fun readyTvTopRated() {
        viewModelScope.launch {
            tvShowRepository.getTvTopRated()
            tvTopRated.value = tvShowRepository.tvTopRated
        }
    }

    fun readyTvOnTheAir() {
        viewModelScope.launch {
            tvShowRepository.getTvOnTheAir()
            tvOnTheAir.value = tvShowRepository.tvOnTheAir
        }
    }

    fun readySimilarTvShows(tvShowId: Int) {
        viewModelScope.launch {
            tvShowRepository.getSimilarTvShows(tvShowId)
            similarTvShows.value = tvShowRepository.similarTvShows
        }
    }

    fun readyTvShowVideos(tvShowId: Int) {
        viewModelScope.launch {
            tvShowRepository.getTvShowVideos(tvShowId)
            tvShowVideos.value = tvShowRepository.tvShowVideos
        }
    }

    fun readyTvShowDetails(tvShowId: Int) {
        viewModelScope.launch {
            tvShowRepository.getTvShowDetails(tvShowId)
            tvShowDetails.value = tvShowRepository.tvShowDetails
        }
    }

    fun readySearch(query: String) {
        viewModelScope.launch {
            tvShowRepository.getSearch(query)
            search.value = tvShowRepository.search
        }
    }

    fun getTvAiringToday(): LiveData<List<Movie>> = tvAiringToday
    fun getTvPopular(): LiveData<List<Movie>> = tvPopular
    fun getTvTopRated(): LiveData<List<Movie>> = tvTopRated
    fun getTvOnTheAir(): LiveData<List<Movie>> = tvOnTheAir
    fun getSimilarTvShows(): LiveData<List<Movie>> = similarTvShows
    fun getTvShowVideos(): LiveData<List<Video>> = tvShowVideos
    fun getTvShowDetails(): LiveData<TvShowDetails> = tvShowDetails
    fun getSearch(): LiveData<List<Movie>> = search
}