package com.example.reviewz.Adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.reviewz.API.MovieApi
import com.example.reviewz.Model.TvShowDetails
import com.example.reviewz.R
import com.example.reviewz.databinding.SeasonItemBinding

class SeasonAdapter(
    private val fragment: Fragment,
    private val seasonList: List<TvShowDetails.Season>
) : RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {

    var duration: Long = 500
    private var onAttach = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        val view = SeasonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeasonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        val season = seasonList[position]
        holder.bind(season)
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount(): Int = seasonList.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class SeasonViewHolder(private val binding: SeasonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(season: TvShowDetails.Season) {
            val posterUrl: String = MovieApi.PHOTO_BASE_URL + season.posterPath

            binding.apply {
                Glide.with(fragment)
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)

                seasonName.text = season.name
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
}