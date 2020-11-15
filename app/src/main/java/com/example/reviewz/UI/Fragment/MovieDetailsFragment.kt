package com.example.reviewz.UI.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.reviewz.API.MovieApi
import com.example.reviewz.Adapter.SecondMovieAdapter
import com.example.reviewz.Model.MovieDetails
import com.example.reviewz.R
import com.example.reviewz.ViewModel.MovieViewModel
import com.example.reviewz.collapse
import com.example.reviewz.databinding.FragmentMovieDetailsBinding
import com.example.reviewz.expand
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

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

        secondMovieAdapter = SecondMovieAdapter(this, true)

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

            movieViewModel.similarMovies(args.movieId).observe(viewLifecycleOwner) {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                binding.recyclerView.scheduleLayoutAnimation()
            }
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
                if (expandLayout.isVisible) {
                    expandButton.animate().setDuration(200).rotation(0F)
                    collapse(expandLayout)
                } else {
                    expandButton.animate().setDuration(200).rotation(180F)
                    expand(expandLayout)
                }
            }

            playVideo.setOnClickListener {
                val action =
                    MovieDetailsFragmentDirections.actionMovieDetailsFragmentToVideoActivity(
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