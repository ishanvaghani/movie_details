package com.example.reviewz.UI.Fragment

import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.reviewz.Adapter.MovieAdapter
import com.example.reviewz.Adapter.SliderAdapter
import com.example.reviewz.R
import com.example.reviewz.ViewModel.MovieViewModel
import com.example.reviewz.databinding.FragmentMovieBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var slideAdapter: SliderAdapter
    private lateinit var popularMovieAdapter: MovieAdapter
    private lateinit var topRatedMovieAdapter: MovieAdapter
    private lateinit var upcomingMoviesAdapter: MovieAdapter

    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        retainInstance = true

        toolbar = binding.toolbar
        toolbar.title = getString(R.string.movie)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        slideAdapter = SliderAdapter(this, ArrayList(), true)
        popularMovieAdapter = MovieAdapter(this, true)
        topRatedMovieAdapter = MovieAdapter(this, true)
        upcomingMoviesAdapter = MovieAdapter(this, true)

        bindUI()
        initViewModel()

        return binding.root
    }

    private fun initViewModel() {
        binding.swipRefreshLayout.isRefreshing = true
        movieViewModel.apply {
            readyNowPlayingMovies()
            getNowPlayingMovies().observe(viewLifecycleOwner, {
                if (it != null) {
                    slideAdapter.setData(it.subList(0, 5))
                }
            })

            movieViewModel.popularMovies.observe(viewLifecycleOwner) {
                popularMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            movieViewModel.topRatedMovies.observe(viewLifecycleOwner) {
                topRatedMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            movieViewModel.upcomingMovies.observe(viewLifecycleOwner) {
                upcomingMoviesAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        binding.swipRefreshLayout.isRefreshing = false
    }

    private fun bindUI() {

        binding.apply {

            swipRefreshLayout.apply {
                setColorSchemeColors(Color.parseColor("#FFD740"))
                setOnRefreshListener(this@MovieFragment)
            }

            imageSlider.apply {
                setIndicatorAnimation(IndicatorAnimationType.WORM)
                setSliderAdapter(slideAdapter)
                startAutoCycle()
            }

            popularRecyclerView.apply {
                setHasFixedSize(true)
                adapter = popularMovieAdapter
            }

            topRatedRecyclerView.apply {
                setHasFixedSize(true)
                adapter = topRatedMovieAdapter
            }

            upcomingRecyclerView.apply {
                setHasFixedSize(true)
                adapter = upcomingMoviesAdapter
            }

            retryButton.setOnClickListener {
                popularMovieAdapter.retry()
                topRatedMovieAdapter.retry()
                upcomingMoviesAdapter.retry()
            }

            popularButton.setOnClickListener {
                val action =
                    MovieFragmentDirections.actionHomeFragmentToViewAllFragment(getString(R.string.popular_movies))
                findNavController().navigate(action)
            }

            topRatedButton.setOnClickListener {
                val action =
                    MovieFragmentDirections.actionHomeFragmentToViewAllFragment(getString(R.string.top_rated_movies))
                findNavController().navigate(action)
            }

            upcomingButton.setOnClickListener {
                val action =
                    MovieFragmentDirections.actionHomeFragmentToViewAllFragment(getString(R.string.upcoming_movies))
                findNavController().navigate(action)
            }
        }

        popularMovieAdapter.addLoadStateListener { loadState ->
            binding.apply {
                swipRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
                linearLayout.isVisible = loadState.source.refresh is LoadState.NotLoading
                errorText.isVisible = loadState.source.refresh is LoadState.Error
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && popularMovieAdapter.itemCount < 1) {
                    linearLayout.isVisible = false
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

    override fun onRefresh() {
        initViewModel()
    }
}