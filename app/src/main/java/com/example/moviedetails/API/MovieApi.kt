package com.example.moviedetails.API

import com.example.moviedetails.Model.*
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

//    https://api.themoviedb.org/3/movie/741074?api_key=1fcbeec0cdad8ddf7fe75e1a6cf41d18
//    https://api.themoviedb.org/3/movie/popular?api_key=1fcbeec0cdad8ddf7fe75e1a6cf41d18

    companion object {
        private const val API_KEY = "1fcbeec0cdad8ddf7fe75e1a6cf41d18"
        const val VIDEO_BASE_URL = "https://www.youtube.com/watch?v="
        const val PHOTO_BASE_URL = "https://image.tmdb.org/t/p/w342"
    }

    //Movies
    @GET("movie/{movie_id}?api_key=$API_KEY")
    suspend fun getMovieDetails(@Path("movie_id")id: Int): Response<MovieDetails>

    @GET("movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovies(): Response<MovieResponse>

    @GET("movie/now_playing?api_key=$API_KEY")
    suspend fun getNowPlayingMovies(): Response<MovieResponse>

    @GET("movie/top_rated?api_key=$API_KEY")
    suspend fun getTopRatedMovies(): Response<MovieResponse>

    @GET("movie/upcoming?api_key=$API_KEY")
    suspend fun getUpcomingMovies(): Response<MovieResponse>

    @GET("movie/{movie_id}/similar?api_key=$API_KEY")
    suspend fun getSimilarMovies(@Path("movie_id")id: Int): Response<MovieResponse>

    @GET("movie/{movie_id}/videos?api_key=$API_KEY")
    suspend fun getMovieVideos(@Path("movie_id")id: Int): Response<VideoResponse>

    //Tv Shows
    @GET("tv/airing_today?api_key=$API_KEY")
    suspend fun getTvAiringToday(): Response<MovieResponse>

    @GET("tv/popular?api_key=$API_KEY")
    suspend fun getTvPopular(): Response<MovieResponse>

    @GET("tv/top_rated?api_key=$API_KEY")
    suspend fun getTvTopRated(): Response<MovieResponse>

    @GET("tv/on_the_air?api_key=$API_KEY")
    suspend fun getTvOnTheAir(): Response<MovieResponse>

    @GET("tv/{tvShow_id}/similar?api_key=$API_KEY")
    suspend fun getSimilarTvShows(@Path("tvShow_id")id: Int): Response<MovieResponse>

    @GET("tv/{tvShow_id}/videos?api_key=$API_KEY")
    suspend fun getTvShowVideos(@Path("tvShow_id")id: Int): Response<VideoResponse>

    @GET("tv/{tvShow_id}?api_key=$API_KEY")
    suspend fun getTvShowDetails(@Path("tvShow_id")id: Int): Response<TvShowDetails>

    @GET("search/multi?api_key=$API_KEY")
    suspend fun getSearch(@Query("query")query: String): Response<MovieResponse>

    //authentication
    @GET("authentication/guest_session/new?api_key=$API_KEY")
    suspend fun getGuestSessionId(): Response<Guest>
}