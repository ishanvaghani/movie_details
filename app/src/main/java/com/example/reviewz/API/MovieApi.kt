package com.example.reviewz.API

import com.example.reviewz.Model.*
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    companion object {
        private const val API_KEY = "1fcbeec0cdad8ddf7fe75e1a6cf41d18"
        const val VIDEO_BASE_URL = "https://www.youtube.com/watch?v="
        const val PHOTO_BASE_URL = "https://image.tmdb.org/t/p/w342"
    }

    //Movies
    @GET("movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movie_id") id: Int): Response<MovieDetails>

    @GET("movie/now_playing?api_key=$API_KEY")
    suspend fun getNowPlayingMovies(): Response<MovieResponse>

    @GET("movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/top_rated?api_key=$API_KEY")
    suspend fun getTopRatedMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/upcoming?api_key=$API_KEY")
    suspend fun getUpcomingMovies(@Query("page") page: Int): MovieResponse

    @GET("movie/{movie_id}/similar?api_key=$API_KEY")
    suspend fun getSimilarMovies(@Path("movie_id") id: Int, @Query("page") page: Int): MovieResponse

    @GET("movie/{movie_id}/videos?api_key=$API_KEY")
    suspend fun getMovieVideos(@Path("movie_id") id: Int): Response<VideoResponse>

    //Tv Shows
    @GET("tv/airing_today?api_key=$API_KEY")
    suspend fun getTvAiringToday(): Response<MovieResponse>

    @GET("tv/popular?api_key=$API_KEY")
    suspend fun getTvPopular(@Query("page") page: Int): MovieResponse

    @GET("tv/top_rated?api_key=$API_KEY")
    suspend fun getTvTopRated(@Query("page") page: Int): MovieResponse

    @GET("tv/on_the_air?api_key=$API_KEY")
    suspend fun getTvOnTheAir(@Query("page") page: Int): MovieResponse

    @GET("tv/{tvShow_id}/similar?api_key=$API_KEY")
    suspend fun getSimilarTvShows(
        @Path("tvShow_id") id: Int,
        @Query("page") page: Int
    ): MovieResponse

    @GET("tv/{tvShow_id}/videos?api_key=$API_KEY")
    suspend fun getTvShowVideos(@Path("tvShow_id") id: Int): Response<VideoResponse>

    @GET("tv/{tvShow_id}?api_key=$API_KEY")
    suspend fun getTvShowDetails(@Path("tvShow_id") id: Int): Response<TvShowDetails>

    @GET("search/multi?api_key=$API_KEY")
    suspend fun getSearch(@Query("query") query: String, @Query("page") page: Int): MovieResponse
}