package com.example.moviedetails.UI.Fragment

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
import com.example.moviedetails.Adapter.MovieAdapter
import com.example.moviedetails.Adapter.SliderAdapter
import com.example.moviedetails.R
import com.example.moviedetails.ViewModel.MovieViewModel
import com.example.moviedetails.databinding.FragmentMovieBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var slideAdapter: SliderAdapter
    private lateinit var popularMovieAdapter: MovieAdapter
    private lateinit var topRatedMovieAdapter: MovieAdapter
    private lateinit var upcomingMovies: MovieAdapter

    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)

        toolbar = binding.toolbar
        toolbar.title = getString(R.string.movie)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        slideAdapter = SliderAdapter(this, ArrayList(), true)
        popularMovieAdapter = MovieAdapter(this, ArrayList(), true)
        topRatedMovieAdapter = MovieAdapter(this, ArrayList(), true)
        upcomingMovies = MovieAdapter(this, ArrayList(), true)

        bindUI()

        movieViewModel.apply {
            readyNowPlayingMovies()
            getNowPlayingMovies().observe(viewLifecycleOwner, {
                if (it != null) {
                    slideAdapter.setData(it.subList(0, 5))
                    showData()
                } else {
                    showError()
                }
            })

            readyPopularMovies()
            getPopularMovie().observe(viewLifecycleOwner, {
                if (it != null) {
                    popularMovieAdapter.setData(it)
                } else {
                    showError()
                }
            })

            readyTopRatedMovies()
            getTopRatedMovies().observe(viewLifecycleOwner, {
                if (it != null) {
                    topRatedMovieAdapter.setData(it)
                } else {
                    showError()
                }
            })

            readyUpcomingMovies()
            getUpcomingMovies().observe(viewLifecycleOwner, {
                if (it != null) {
                    upcomingMovies.setData(it)
                } else {
                    showError()
                }
            })
        }

        return binding.root
    }

    private fun bindUI() {
        binding.imageSlider.apply {
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderAdapter(slideAdapter)
            startAutoCycle()
        }

        binding.popularRecyclerView.apply {
            setHasFixedSize(true)
            adapter = popularMovieAdapter
        }

        binding.topRatedRecyclerView.apply {
            setHasFixedSize(true)
            adapter = topRatedMovieAdapter
        }

        binding.upcomingRecyclerView.apply {
            setHasFixedSize(true)
            adapter = upcomingMovies
        }

        binding.popularButton.setOnClickListener {
            val action =
                MovieFragmentDirections.actionHomeFragmentToViewAllFragment(getString(R.string.popular_movies))
            findNavController().navigate(action)
        }

        binding.topRatedButton.setOnClickListener {
            val action =
                MovieFragmentDirections.actionHomeFragmentToViewAllFragment(getString(R.string.top_rated_movies))
            findNavController().navigate(action)
        }

        binding.upcomingButton.setOnClickListener {
            val action =
                MovieFragmentDirections.actionHomeFragmentToViewAllFragment(getString(R.string.upcoming_movies))
            findNavController().navigate(action)
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