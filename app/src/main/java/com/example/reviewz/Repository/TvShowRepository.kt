package com.example.reviewz.Repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.reviewz.*
import com.example.reviewz.API.ApiBuilder
import com.example.reviewz.API.MovieApi
import com.example.reviewz.Model.Movie
import com.example.reviewz.Model.TvShowDetails
import com.example.reviewz.Model.Video
import com.example.reviewz.Pagination.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val movieApi: MovieApi,
    private val apiBuilder: ApiBuilder,
    @ApplicationContext private val context: Context
) {

    var tvAiringToday: List<Movie>? = null
    var tvShowVideos: List<Video>? = null
    var tvShowDetails: TvShowDetails? = null

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

    fun getPopularTvShow() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PopularTvShowPagingSource(movieApi) }
    ).liveData

    fun getTopRateTvShow() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TopRatedTvShowPagingSource(movieApi) }
    ).liveData

    fun getOnTheAirTvShow() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { OnTheAirTvShowPagingSource(movieApi) }
    ).liveData

    fun getSimilarTvShow(tvShowId: Int) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { SimilarTvShowPagingSource(tvShowId, movieApi) }
    ).liveData

    fun getSearch(query: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { SearchPagingSource(query, movieApi) }
    ).liveData
}