package com.example.moviedetails.Adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.moviedetails.API.MovieApi
import com.example.moviedetails.Model.Movie
import com.example.moviedetails.R
import com.example.moviedetails.UI.Fragment.MovieDetailsFragmentDirections
import com.example.moviedetails.UI.Fragment.SearchFragmentDirections
import com.example.moviedetails.UI.Fragment.TvShowDetailsFragmentDirections
import com.example.moviedetails.UI.Fragment.ViewAllFragmentDirections
import com.example.moviedetails.databinding.SecondMovieItemBinding


class SecondMovieAdapter(
    private val fragment: Fragment,
    private var movieList: List<Movie>,
    private val isMovie: Boolean
) :
    RecyclerView.Adapter<SecondMovieAdapter.SecondMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondMovieViewHolder {
        val view =
            SecondMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SecondMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SecondMovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movieList.size

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
                        if(movie.title == null) {
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
                    if (fragment.findNavController().currentDestination?.id == R.id.movieDetailsFragment) {
                        val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(
                            movie.id,
                            movie.title
                        )
                        fragment.findNavController().navigate(action)
                    }
                }
                else {
                    if (fragment.findNavController().currentDestination?.id == R.id.viewAllFragment) {
                        val action =
                            ViewAllFragmentDirections.actionViewAllFragmentToTvShowDetailsFragment(
                                movie.id,
                                movie.name
                            )
                        fragment.findNavController().navigate(action)
                    }
                    if (fragment.findNavController().currentDestination?.id == R.id.tvShowDetailsFragment) {
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

    fun setData(movieList: List<Movie>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }
}