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
import androidx.paging.LoadState
import com.example.reviewz.Adapter.MovieAdapter
import com.example.reviewz.Adapter.SliderAdapter
import com.example.reviewz.R
import com.example.reviewz.ViewModel.TvShowViewModel
import com.example.reviewz.databinding.FragmentTvShowBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowFragment : Fragment() {

    private lateinit var slideAdapter: SliderAdapter
    private lateinit var tvPopularAdapter: MovieAdapter
    private lateinit var tvTopRatedAdapter: MovieAdapter
    private lateinit var tvOnTheAirAdapter: MovieAdapter

    private var _binding: FragmentTvShowBinding? = null
    private val binding get() = _binding!!

    private val tvShowViewModel: TvShowViewModel by viewModels()

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvShowBinding.inflate(inflater, container, false)

        toolbar = binding.toolbar
        toolbar.title = getString(R.string.tv_show)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        slideAdapter = SliderAdapter(this, ArrayList(), false)
        tvPopularAdapter = MovieAdapter(this, false)
        tvTopRatedAdapter = MovieAdapter(this, false)
        tvOnTheAirAdapter = MovieAdapter(this, false)

        bindUI()

        tvShowViewModel.apply {
            readyTvAiringToday()
            getTvAiringToday().observe(viewLifecycleOwner, {
                if (it != null) {
                    slideAdapter.setData(it.subList(0, 5))
                }
            })

            tvShowViewModel.onTheAirTvShow.observe(viewLifecycleOwner) {
                tvOnTheAirAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            tvShowViewModel.popularTvShow.observe(viewLifecycleOwner) {
                tvPopularAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            tvShowViewModel.topRatedTvShow.observe(viewLifecycleOwner) {
                tvTopRatedAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        return binding.root
    }

    private fun bindUI() {
        binding.apply {
            imageSlider.apply {
                setIndicatorAnimation(IndicatorAnimationType.WORM)
                setSliderAdapter(slideAdapter)
                startAutoCycle()
            }

            popularRecyclerView.apply {
                setHasFixedSize(true)
                adapter = tvPopularAdapter
            }

            topRatedRecyclerView.apply {
                setHasFixedSize(true)
                adapter = tvTopRatedAdapter
            }

            onTheAirRecyclerView.apply {
                setHasFixedSize(true)
                adapter = tvOnTheAirAdapter
            }

            retryButton.setOnClickListener {
                tvPopularAdapter.retry()
                tvTopRatedAdapter.retry()
                tvOnTheAirAdapter.retry()
            }

            popularButton.setOnClickListener {
                val action =
                    TvShowFragmentDirections.actionTvShowFragmentToViewAllFragment(getString(R.string.popular_tv_shows))
                findNavController().navigate(action)
            }

            topRatedButton.setOnClickListener {
                val action =
                    TvShowFragmentDirections.actionTvShowFragmentToViewAllFragment(getString(R.string.top_rated_tv_shows))
                findNavController().navigate(action)
            }

            onTheAirButton.setOnClickListener {
                val action =
                    TvShowFragmentDirections.actionTvShowFragmentToViewAllFragment(getString(R.string.on_the_air_tv_shows))
                findNavController().navigate(action)
            }
        }

        tvPopularAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                linearLayout.isVisible = loadState.source.refresh is LoadState.NotLoading
                errorText.isVisible = loadState.source.refresh is LoadState.Error
                retryButton.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && tvPopularAdapter.itemCount < 1) {
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
}