package com.example.reviewz.UI.Fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reviewz.Adapter.GenreAdapter
import com.example.reviewz.Adapter.SecondMovieAdapter
import com.example.reviewz.R
import com.example.reviewz.ViewModel.MovieViewModel
import com.example.reviewz.ViewModel.TvShowViewModel
import com.example.reviewz.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), GenreAdapter.OnGenreClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val tvShowViewModel: TvShowViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var secondMovieAdapter: SecondMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        secondMovieAdapter = SecondMovieAdapter(this, true)

        bindUI()

        binding.apply {
            editText.setOnEditorActionListener { _, actionId, _ ->
                emptyState.isVisible = false
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    tvShowViewModel.apply {
                        tvShowViewModel.search(editText.text.toString().trim())
                            .observe(viewLifecycleOwner) {
                                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                                binding.recyclerView.scheduleLayoutAnimation()
                                editText.clearFocus()
                            }
                    }
                }
                true
            }
        }
        return binding.root
    }

    private fun bindUI() {

        binding.apply {
            moviesGenreRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 2)
                adapter = GenreAdapter(
                    context, movieGenres,
                    this@SearchFragment,
                    context.getString(R.string.movie),
                )
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val position = parent.getChildAdapterPosition(view)
                        outRect.left =
                            if (position % 2 == 0) 0 else 10
                        outRect.right =
                            if (position % 2 == 0) 10 else 0
                        outRect.bottom = 20
                        if (position == 0 || position == 1) {
                            outRect.top = 20
                        }
                    }
                })
            }

            tvShowsGenreRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val position = parent.getChildAdapterPosition(view)
                        outRect.left =
                            if (position % 2 == 0) 0 else 10
                        outRect.right =
                            if (position % 2 == 0) 10 else 0
                        outRect.bottom = 20
                        if (position == 0 || position == 1) {
                            outRect.top = 20
                        }
                    }
                })
                adapter = GenreAdapter(
                    context,
                    tvShowGenres,
                    this@SearchFragment,
                    context.getString(R.string.tv_show),
                )
            }
        }

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = secondMovieAdapter
        }

        secondMovieAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                if (binding.editText.text.isNotEmpty()) {
                    recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                }
                errorText.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && secondMovieAdapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    emptyState.isVisible = true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onGenreClick(position: Int, type: String) {
        binding.emptyState.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        if(type == context?.getString(R.string.tv_show)) {
            tvShowViewModel.getGenreTVShows(tvShowGenres[position].id).observe(viewLifecycleOwner, {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                binding.recyclerView.scheduleLayoutAnimation()
            })
        } else {
            movieViewModel.getGenreMovies(movieGenres[position].id).observe(viewLifecycleOwner, {
                secondMovieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                binding.recyclerView.scheduleLayoutAnimation()
            })
        }
    }
}