package com.example.moviedetails.Repository

import android.content.Context
import android.util.Log
import android.view.View
import com.example.moviedetails.API.ApiBuilder
import com.example.moviedetails.Model.*
import com.example.moviedetails.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiBuilder: ApiBuilder,
    @ApplicationContext private val context: Context
) {

    var movieDetails: MovieDetails? = null
    var popularMovies: List<Movie>? = null
    var nowPlayingMovies: List<Movie>? = null
    var topRatedMovies: List<Movie>? = null
    var upcomingMovies: List<Movie>? = null
    var similarMovies: List<Movie>? = null
    var movieVideos: List<Video>? = null

    suspend fun getMovieDetails(movieId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getMovieDetails(movieId)
            if (response.isSuccessful) {
                movieDetails = response.body()!!
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            withContext(Dispatchers.Main) {
                showToast(context)
            }
        }
    }

    suspend fun getPopularMovies() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getPopularMovies()
            if (response.isSuccessful) {
                popularMovies = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getNowPlayingMovies() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getNowPlayingMovies()
            if (response.isSuccessful) {
                nowPlayingMovies = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getTopRatedMovies() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getTopRatedMovies()
            if (response.isSuccessful) {
                topRatedMovies = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getUpcomingMovies() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getUpcomingMovies()
            if (response.isSuccessful) {
                upcomingMovies = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getSimilarMovies(movieId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getSimilarMovies(movieId)
            if (response.isSuccessful) {
                similarMovies = response.body()!!.results
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }

    suspend fun getMovieVideos(movieId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getMovieVideos(movieId)
            if (response.isSuccessful) {
                movieVideos = response.body()!!.results
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