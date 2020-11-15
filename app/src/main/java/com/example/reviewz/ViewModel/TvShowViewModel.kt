package com.example.reviewz.ViewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.reviewz.Model.Movie
import com.example.reviewz.Model.TvShowDetails
import com.example.reviewz.Model.Video
import com.example.reviewz.Repository.TvShowRepository
import kotlinx.coroutines.launch

class TvShowViewModel @ViewModelInject constructor(private val tvShowRepository: TvShowRepository) :
    ViewModel() {

    private var tvAiringToday: MutableLiveData<List<Movie>> = MutableLiveData()
    private var tvShowVideos: MutableLiveData<List<Video>> = MutableLiveData()
    private var tvShowDetails: MutableLiveData<TvShowDetails> = MutableLiveData()

    fun readyTvAiringToday() {
        viewModelScope.launch {
            tvShowRepository.getTvAiringToday()
            tvAiringToday.value = tvShowRepository.tvAiringToday
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

    val popularTvShow = tvShowRepository.getPopularTvShow().cachedIn(viewModelScope)
    val topRatedTvShow = tvShowRepository.getTopRateTvShow().cachedIn(viewModelScope)
    val onTheAirTvShow = tvShowRepository.getOnTheAirTvShow().cachedIn(viewModelScope)
    fun similarTvShow(tvShowId: Int) =
        tvShowRepository.getSimilarTvShow(tvShowId).cachedIn(viewModelScope)

    fun search(query: String) = tvShowRepository.getSearch(query).cachedIn(viewModelScope)

    fun getTvAiringToday(): LiveData<List<Movie>> = tvAiringToday
    fun getTvShowVideos(): LiveData<List<Video>> = tvShowVideos
    fun getTvShowDetails(): LiveData<TvShowDetails> = tvShowDetails
}