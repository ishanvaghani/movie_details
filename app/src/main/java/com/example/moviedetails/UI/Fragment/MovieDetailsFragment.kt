package com.example.moviedetails.UI.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_APPEND
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.moviedetails.API.MovieApi
import com.example.moviedetails.Adapter.SecondMovieAdapter
import com.example.moviedetails.Model.MovieDetails
import com.example.moviedetails.Model.Rate
import com.example.moviedetails.R
import com.example.moviedetails.ViewModel.MovieViewModel
import com.example.moviedetails.collapse
import com.example.moviedetails.databinding.FragmentMovieDetailsBinding
import com.example.moviedetails.databinding.RateLayoutBinding
import com.example.moviedetails.expand
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var secondMovieAdapter: SecondMovieAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        toolbar = binding.toolbar
        toolbar.title = args.title
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        secondMovieAdapter = SecondMovieAdapter(this, ArrayList(), true)

        movieViewModel.apply {
            readyMovieDetails(args.movieId)
            getMovieDetails().observe(viewLifecycleOwner, {
                if (it != null) {
                    bindWithUi(it, container!!)
                    showData()
                } else {
                    showError()
                }
            })

            readySimilarMovies(args.movieId)
            getSimilarMovies().observe(viewLifecycleOwner, {
                if (it != null) {
                    secondMovieAdapter.setData(it)
                    binding.recyclerView.scheduleLayoutAnimation()
                }
            })
        }
        return binding.root
    }

    private fun bindWithUi(movieDetails: MovieDetails, container: ViewGroup) {
        binding.apply {
            movieTitle.text = movieDetails.title
            movieTagline.text = movieDetails.tagline
            movieReleaseDate.text = movieDetails.releaseDate
            movieRating.text = movieDetails.rating.toString()
            movieRuntime.text = movieDetails.runtime.toString() + R.string.minutes
            movieOverview.text = movieDetails.overview

            val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
            movieBudget.text = formatCurrency.format(movieDetails.budget)
            movieRevenue.text = formatCurrency.format(movieDetails.revenue)

            val posterUrl: String = MovieApi.PHOTO_BASE_URL + movieDetails.posterPath
            Glide.with(this@MovieDetailsFragment)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(poster)

            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 3)
                adapter = secondMovieAdapter
            }

            expandButton.setOnClickListener {
                if(expandLayout.isVisible) {
                    expandButton.animate().setDuration(200).rotation(0F)
                    collapse(expandLayout)
                }
                else {
                    expandButton.animate().setDuration(200).rotation(180F)
                    expand(expandLayout)
                }
            }

            playVideo.setOnClickListener {
                val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentToVideoActivity(
                    movieDetails.id, getString(
                        R.string.movie
                    )
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun showData() {
        binding.apply {
            errorText.isVisible = false
            linearLayout.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            errorText.isVisible = true
            linearLayout.isVisible = false
            progressBar.isVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}