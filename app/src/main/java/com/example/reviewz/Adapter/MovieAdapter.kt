package com.example.reviewz.Adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.reviewz.API.MovieApi
import com.example.reviewz.Model.Movie
import com.example.reviewz.R
import com.example.reviewz.UI.Fragment.MovieDetailsFragmentDirections
import com.example.reviewz.UI.Fragment.MovieFragmentDirections
import com.example.reviewz.UI.Fragment.TvShowDetailsFragmentDirections
import com.example.reviewz.UI.Fragment.TvShowFragmentDirections
import com.example.reviewz.databinding.MovieItemBinding

class MovieAdapter(
    private val fragment: Fragment,
    private val isMovie: Boolean,
) :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(COMPARATOR) {

    var duration: Long = 500
    private var onAttach = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie!!)
        setAnimation(holder.itemView, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class MovieViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(movie: Movie) {
            val posterUrl: String = MovieApi.PHOTO_BASE_URL + movie.posterPath
            Glide.with(fragment)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)

            binding.root.setOnClickListener {
                if (isMovie) {
                    if (fragment.findNavController().currentDestination?.id == R.id.homeFragment) {
                        val action =
                            MovieFragmentDirections.actionHomeFragmentToMovieDetailsFragment(
                                movie.id,
                                movie.title
                            )
                        fragment.findNavController().navigate(action)
                    }
                    if (fragment.findNavController().currentDestination?.id == R.id.movieDetailsFragment) {
                        val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(
                            movie.id,
                            movie.title
                        )
                        fragment.findNavController().navigate(action)
                    }
                } else {
                    if(fragment.findNavController().currentDestination?.id == R.id.tvShowFragment) {
                        val action =
                            TvShowFragmentDirections.actionTvShowFragmentToTvShowDetailsFragment(
                                movie.id,
                                movie.name
                            )
                        fragment.findNavController().navigate(action)
                    }
                    if(fragment.findNavController().currentDestination?.id == R.id.tvShowDetailsFragment) {
                        val action =
                            TvShowDetailsFragmentDirections.actionTvShowDetailsFragmentSelf(
                                movie.id,
                                movie.name
                            )
                        fragment.findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun setAnimation(itemView: View, position: Int) {
        var i = position
        if (!onAttach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.startDelay = if (isNotFirstItem) duration / 2 else i * duration / 3
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem == newItem
        }
    }
}