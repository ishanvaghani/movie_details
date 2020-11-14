package com.example.moviedetails.UI.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedetails.Adapter.SecondMovieAdapter
import com.example.moviedetails.ViewModel.MovieViewModel
import com.example.moviedetails.ViewModel.TvShowViewModel
import com.example.moviedetails.databinding.FragmentSearchBinding
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

        secondMovieAdapter = SecondMovieAdapter(this, ArrayList(), true)

        bindUI()

        binding.apply {
            editText.setOnEditorActionListener { _, actionId, _ ->
                progressBar.isVisible = true
                searchEmpty.isVisible = false
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    tvShowViewModel.apply {
                        readySearch(editText.text.toString().trim())
                        getSearch().observe(viewLifecycleOwner, {
                            progressBar.isVisible = false
                            if(it != null) {
                                secondMovieAdapter.setData(it)
                                binding.recyclerView.scheduleLayoutAnimation()
                                editText.clearFocus()
                            } else {
                                errorText.isVisible = true
                            }
                        })
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}