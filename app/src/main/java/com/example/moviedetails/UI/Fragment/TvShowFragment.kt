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
import com.example.moviedetails.ViewModel.TvShowViewModel
import com.example.moviedetails.databinding.FragmentTvShowBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowFragment: Fragment() {

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
        tvPopularAdapter = MovieAdapter(this, ArrayList(), false)
        tvTopRatedAdapter = MovieAdapter(this, ArrayList(), false)
        tvOnTheAirAdapter = MovieAdapter(this, ArrayList(), false)

        bindUI()

        tvShowViewModel.apply {
            readyTvAiringToday()
            getTvAiringToday().observe(viewLifecycleOwner, {
                if (it != null) {
                    slideAdapter.setData(it.subList(0, 5))
                    showData()
                } else {
                    showError()
                }
            })

            readyTvOnTheAir()
            getTvOnTheAir().observe(viewLifecycleOwner, {
                if (it != null) {
                    tvOnTheAirAdapter.setData(it)
                } else {
                    showError()
                }
            })

            readyTvPopular()
            getTvPopular().observe(viewLifecycleOwner, {
                if (it != null) {
                    tvPopularAdapter.setData(it)
                } else {
                    showError()
                }
            })

            readyTvTopRated()
            getTvTopRated().observe(viewLifecycleOwner, {
                if (it != null) {
                    tvTopRatedAdapter.setData(it)
                } else {
                    showError()
                }
            })
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