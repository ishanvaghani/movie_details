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
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reviewz.Adapter.SecondMovieAdapter
import com.example.reviewz.Pagination.MovieLoadStateAdapter
import com.example.reviewz.R
import com.example.reviewz.ViewModel.MovieViewModel
import com.example.reviewz.ViewModel.TvShowViewModel
import com.example.reviewz.databinding.FragmentViewAllBinding
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

        if (args.type == getString(R.string.popular_movies) || args.type == getString(R.string.top_rated_movies) || args.type == getString(
                R.string.upcoming_movies
            )
        ) {
            secondMovieAdapter = SecondMovieAdapter(this, true)
        } else {
            secondMovieAdapter = SecondMovieAdapter(this, false)
        }

        bindUI()

        return binding.root
    }

    private fun bindUI() {
        if (args.type == getString(R.string.popular_movies)) {
            movieViewModel.popularMovies.observe(viewLifecycleOwner) {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        if (args.type == getString(R.string.top_rated_movies)) {
            movieViewModel.topRatedMovies.observe(viewLifecycleOwner) {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        if (args.type == getString(R.string.upcoming_movies)) {
            movieViewModel.upcomingMovies.observe(viewLifecycleOwner) {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        if (args.type == getString(R.string.popular_tv_shows)) {
            tvShowViewModel.popularTvShow.observe(viewLifecycleOwner) {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        if (args.type == getString(R.string.top_rated_tv_shows)) {
            tvShowViewModel.topRatedTvShow.observe(viewLifecycleOwner) {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        if (args.type == getString(R.string.on_the_air_tv_shows)) {
            tvShowViewModel.onTheAirTvShow.observe(viewLifecycleOwner) {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        binding.apply {
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 3)
                adapter = secondMovieAdapter.withLoadStateHeaderAndFooter(
                    header = MovieLoadStateAdapter { secondMovieAdapter.retry() },
                    footer = MovieLoadStateAdapter { secondMovieAdapter.retry() }
                )
            }
            retryButton.setOnClickListener {
                secondMovieAdapter.retry()
            }
        }

        secondMovieAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                errorText.isVisible = loadState.source.refresh is LoadState.Error
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && secondMovieAdapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    errorText.isVisible = true
                } else {
                    errorText.isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}