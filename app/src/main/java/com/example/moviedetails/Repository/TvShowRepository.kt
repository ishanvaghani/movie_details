package com.example.moviedetails.Repository

import android.content.Context
import com.example.moviedetails.API.ApiBuilder
import com.example.moviedetails.Model.Movie
import com.example.moviedetails.Model.TvShowDetails
import com.example.moviedetails.Model.Video
import com.example.moviedetails.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val apiBuilder: ApiBuilder,
    @ApplicationContext private val context: Context
) {

    var tvAiringToday: List<Movie>? = null
    var tvPopular: List<Movie>? = null
    var tvTopRated: List<Movie>? = null
    var tvOnTheAir: List<Movie>? = null
    var similarTvShows: List<Movie>? = null
    var tvShowVideos: List<Video>? = null
    var tvShowDetails: TvShowDetails? = null
    var search: List<Movie>? = null

    suspend fun getTvAiringToday() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getTvAiringToday()
            if (response.isSuccessful) {
                tvAiringToday = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getTvPopular() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getTvPopular()
            if (response.isSuccessful) {
                tvPopular = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getTvTopRated() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getTvTopRated()
            if (response.isSuccessful) {
                tvTopRated = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getTvOnTheAir() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getTvOnTheAir()
            if (response.isSuccessful) {
                tvOnTheAir = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getSimilarTvShows(tvShowId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getSimilarTvShows(tvShowId)
            if (response.isSuccessful) {
                similarTvShows = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getTvShowVideos(tvShowId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getTvShowVideos(tvShowId)
            if (response.isSuccessful) {
                tvShowVideos = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getTvShowDetails(tvShowId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getTvShowDetails(tvShowId)
            if (response.isSuccessful) {
                tvShowDetails = response.body()!!
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getSearch(query: String) = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getSearch(query)
            if (response.isSuccessful) {
                search = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }
}