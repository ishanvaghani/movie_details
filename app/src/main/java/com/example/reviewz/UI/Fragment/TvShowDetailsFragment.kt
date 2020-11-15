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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.reviewz.API.MovieApi
import com.example.reviewz.Adapter.MovieAdapter
import com.example.reviewz.Adapter.SeasonAdapter
import com.example.reviewz.Model.TvShowDetails
import com.example.reviewz.R
import com.example.reviewz.ViewModel.TvShowViewModel
import com.example.reviewz.collapse
import com.example.reviewz.databinding.FragmentTvshowDetailsBinding
import com.example.reviewz.expand
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TvShowDetailsFragment : Fragment() {

    private var _binding: FragmentTvshowDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: TvShowDetailsFragmentArgs by navArgs()
    private val tvShowViewModel: TvShowViewModel by viewModels()

    private lateinit var similarAdapter: MovieAdapter
    private lateinit var seasonAdapter: SeasonAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvshowDetailsBinding.inflate(inflater, container, false)

        toolbar = binding.toolbar
        toolbar.title = args.title
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        similarAdapter = MovieAdapter(this, false)

        tvShowViewModel.apply {
            readyTvShowDetails(args.tvShowId)
            getTvShowDetails().observe(viewLifecycleOwner, {
                if (it != null) {
                    bindWithUi(it)
                    showData()
                } else {
                    showError()
                }
            })

            tvShowViewModel.similarTvShow(args.tvShowId).observe(viewLifecycleOwner) {
                similarAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                binding.similarRecyclerView.scheduleLayoutAnimation()
            }
        }
        return binding.root
    }

    private fun bindWithUi(tvShowDetails: TvShowDetails) {

        seasonAdapter = SeasonAdapter(this, tvShowDetails.seasons)

        binding.apply {
            movieTitle.text = tvShowDetails.name
            movieTagline.text = tvShowDetails.tagline
            firstReleaseDate.text = tvShowDetails.firstRelease
            lastReleaseDate.text = tvShowDetails.lastRelease
            movieRating.text = tvShowDetails.rating.toString()
            noOfSeason.text = tvShowDetails.numberOfSeasons.toString()
            movieOverview.text = tvShowDetails.overview
            noOfEpisode.text = tvShowDetails.numberOfEpisodes.toString()

            val posterUrl: String = MovieApi.PHOTO_BASE_URL + tvShowDetails.posterPath
            Glide.with(this@TvShowDetailsFragment)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(poster)

            similarRecyclerView.apply {
                setHasFixedSize(true)
                adapter = similarAdapter
            }

            seasonsRecyclerView.apply {
                setHasFixedSize(true)
                adapter = seasonAdapter
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
                    TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentToVideoActivity(
                        tvShowDetails.id,
                        getString(R.string.tv_show)
                    )
                findNavController().navigate(action)
            }
        }

        similarAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                linearLayout.isVisible = loadState.source.refresh is LoadState.NotLoading
                errorText.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && similarAdapter.itemCount < 1) {
                    viewSimilar.isVisible = false
                    textSimilar.isVisible = false
                }
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}