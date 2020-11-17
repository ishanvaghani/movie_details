package com.example.reviewz.Adapter

import android.view.LayoutInflater
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
import com.example.reviewz.UI.Fragment.SearchFragmentDirections
import com.example.reviewz.UI.Fragment.ViewAllFragmentDirections
import com.example.reviewz.databinding.SecondMovieItemBinding


class SecondMovieAdapter(
    private val fragment: Fragment,
    private val isMovie: Boolean
) :
    PagingDataAdapter<Movie, SecondMovieAdapter.SecondMovieViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondMovieViewHolder {
        val view =
            SecondMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SecondMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SecondMovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie!!)
    }

    inner class SecondMovieViewHolder(private val binding: SecondMovieItemBinding) :
        RecyclerView.ViewHolder(
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
                    if (fragment.findNavController().currentDestination?.id == R.id.searchFragment) {
                        if (movie.title == null) {
                            val action =
                                SearchFragmentDirections.actionSearchFragmentToTvShowDetailsFragment(
                                    movie.id,
                                    movie.name
                                )
                            fragment.findNavController().navigate(action)
                        } else {
                            val action =
                                SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(
                                    movie.id,
                                    movie.title
                                )
                            fragment.findNavController().navigate(action)
                        }
                    }
                    if (fragment.findNavController().currentDestination?.id == R.id.viewAllFragment) {
                        val action =
                            ViewAllFragmentDirections.actionViewAllFragmentToMovieDetailsFragment(
                                movie.id,
                                movie.title
                            )
                        fragment.findNavController().navigate(action)
                    }
                } else {
                    if (fragment.findNavController().currentDestination?.id == R.id.viewAllFragment) {
                        val action =
                            ViewAllFragmentDirections.actionViewAllFragmentToTvShowDetailsFragment(
                                movie.id,
                                movie.name
                            )
                        fragment.findNavController().navigate(action)
                    }
                }
            }
        }
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