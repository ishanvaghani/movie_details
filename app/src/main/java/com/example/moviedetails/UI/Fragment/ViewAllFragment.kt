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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedetails.Adapter.SecondMovieAdapter
import com.example.moviedetails.R
import com.example.moviedetails.ViewModel.MovieViewModel
import com.example.moviedetails.ViewModel.TvShowViewModel
import com.example.moviedetails.databinding.FragmentViewAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllFragment : Fragment() {

    private var _binding: FragmentViewAllBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel: MovieViewModel by viewModels()
    private val tvShowViewModel: TvShowViewModel by viewModels()

    private lateinit var secondMovieAdapter: SecondMovieAdapter
    private lateinit var toolbar: Toolbar

    private val args: ViewAllFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewAllBinding.inflate(layoutInflater, container, false)

        toolbar = binding.toolbar
        toolbar.title = args.type
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(args.type == getString(R.string.popular_movies) || args.type == getString(R.string.top_rated_movies) || args.type == getString(R.string.upcoming_movies)) {
            secondMovieAdapter = SecondMovieAdapter(this, ArrayList(), true)
        }
        else {
            secondMovieAdapter = SecondMovieAdapter(this, ArrayList(), false)
        }

        bindUI()

        return binding.root
    }

    private fun bindUI() {
        if (args.type == getString(R.string.popular_movies)) {
            movieViewModel.apply {
                readyPopularMovies()
                getPopularMovie().observe(viewLifecycleOwner, {
                    if (it != null) {
                        secondMovieAdapter.setData(it)
                        binding.recyclerView.scheduleLayoutAnimation()
                        showData()
                    } else {
                        showError()
                    }
                })
            }
        }
        if (args.type == getString(R.string.top_rated_movies)) {
            movieViewModel.apply {
                readyTopRatedMovies()
                getTopRatedMovies().observe(viewLifecycleOwner, {
                    if (it != null) {
                        secondMovieAdapter.setData(it)
                        binding.recyclerView.scheduleLayoutAnimation()
                        showData()
                    } else {
                        showError()
                    }
                })
            }
        }
        if (args.type == getString(R.string.upcoming_movies)) {
            movieViewModel.apply {
                readyUpcomingMovies()
                getUpcomingMovies().observe(viewLifecycleOwner, {
                    if (it != null) {
                        secondMovieAdapter.setData(it)
                        binding.recyclerView.scheduleLayoutAnimation()
                        showData()
                    } else {
                        showError()
                    }
                })
            }
        }
        if (args.type == getString(R.string.popular_tv_shows)) {
            tvShowViewModel.apply {
                readyTvPopular()
                getTvPopular().observe(viewLifecycleOwner, {
                    if (it != null) {
                        secondMovieAdapter.setData(it)
                        binding.recyclerView.scheduleLayoutAnimation()
                        showData()
                    } else {
                        showError()
                    }
                })
            }
        }
        if (args.type == getString(R.string.top_rated_tv_shows)) {
            tvShowViewModel.apply {
                readyTvTopRated()
                getTvTopRated().observe(viewLifecycleOwner, {
                    if (it != null) {
                        secondMovieAdapter.setData(it)
                        binding.recyclerView.scheduleLayoutAnimation()
                        showData()
                    } else {
                        showError()
                    }
                })
            }
        }
        if (args.type == getString(R.string.on_the_air_tv_shows)) {
            tvShowViewModel.apply {
                readyTvOnTheAir()
                getTvOnTheAir().observe(viewLifecycleOwner, {
                    if (it != null) {
                        secondMovieAdapter.setData(it)
                        binding.recyclerView.scheduleLayoutAnimation()
                        showData()
                    } else {
                        showError()
                    }
                })
            }
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = secondMovieAdapter
        }
    }

    private fun showData() {
        binding.apply {
            recyclerView.isVisible = true
            progressBar.isVisible = false
            errorText.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            recyclerView.isVisible = false
            progressBar.isVisible = false
            errorText.isVisible = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}