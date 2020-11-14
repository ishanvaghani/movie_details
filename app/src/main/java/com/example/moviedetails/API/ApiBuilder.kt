package com.example.moviedetails.API

import com.example.moviedetails.Model.*
import retrofit2.Response
import javax.inject.Inject

class ApiBuilder @Inject constructor(private val movieApi: MovieApi) {

    //Movies
    suspend fun getMovieDetails(movieId: Int): Response<MovieDetails> = movieApi.getMovieDetails(movieId)

    suspend fun getPopularMovies(): Response<MovieResponse> = movieApi.getPopularMovies()

    suspend fun getNowPlayingMovies(): Response<MovieResponse> = movieApi.getNowPlayingMovies()

    suspend fun getTopRatedMovies(): Response<MovieResponse> = movieApi.getTopRatedMovies()

    suspend fun getUpcomingMovies(): Response<MovieResponse> = movieApi.getUpcomingMovies()

    suspend fun getSimilarMovies(movieId: Int): Response<MovieResponse> = movieApi.getSimilarMovies(movieId)

    suspend fun getMovieVideos(movieId: Int): Response<VideoResponse> = movieApi.getMovieVideos(movieId)

    //Tv shows
    suspend fun getTvAiringToday(): Response<MovieResponse> = movieApi.getTvAiringToday()

    suspend fun getTvPopular(): Response<MovieResponse> = movieApi.getTvPopular()

    suspend fun getTvTopRated(): Response<MovieResponse> = movieApi.getTvTopRated()

    suspend fun getTvOnTheAir(): Response<MovieResponse> = movieApi.getTvOnTheAir()

    suspend fun getSimilarTvShows(tvShowId: Int): Response<MovieResponse> = movieApi.getSimilarTvShows(tvShowId)

    suspend fun getTvShowVideos(tvShowId: Int): Response<VideoResponse> = movieApi.getTvShowVideos(tvShowId)

    suspend fun getTvShowDetails(tvShowId: Int): Response<TvShowDetails> = movieApi.getTvShowDetails(tvShowId)

    suspend fun getSearch(query: String): Response<MovieResponse> = movieApi.getSearch(query)
}