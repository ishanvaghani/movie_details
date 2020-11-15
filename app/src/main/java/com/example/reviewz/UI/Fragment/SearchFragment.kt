package com.example.reviewz.UI.Fragment

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
import com.example.reviewz.Adapter.SecondMovieAdapter
import com.example.reviewz.ViewModel.TvShowViewModel
import com.example.reviewz.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val tvShowViewModel: TvShowViewModel by viewModels()
    private lateinit var secondMovieAdapter: SecondMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        secondMovieAdapter = SecondMovieAdapter(this, true)

        bindUI()

        binding.apply {
            editText.setOnEditorActionListener { _, actionId, _ ->
                searchEmpty.isVisible = false
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
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = secondMovieAdapter
        }

        secondMovieAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                errorText.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && secondMovieAdapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    searchEmpty.isVisible = true
                } else {
                    searchEmpty.isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}