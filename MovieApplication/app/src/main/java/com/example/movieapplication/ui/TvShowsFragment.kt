package com.example.movieapplication.ui

import android.view.View
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.BaseFragment
import com.example.movieapplication.databinding.TvShowsFragmentBinding
import com.example.movieapplication.ui.adapters.TvShowsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvShowsFragment : BaseFragment<TvShowsFragmentBinding>(TvShowsFragmentBinding :: inflate) {

    private val tvShowsViewModel: TvShowsViewModel by viewModels()
    private lateinit var tvShowsAdapter: TvShowsPagingAdapter
    override fun start() {
        initRecycler()
        goToDetailsFragment()
        searchView()
        displayProgressBar()
        fetchTvShows()
    }
    private fun initRecycler(){
        tvShowsAdapter = TvShowsPagingAdapter()
        binding.rvTvShow.adapter = tvShowsAdapter
        binding.rvTvShow.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fetchTvShows() {
        lifecycleScope.launch {
            tvShowsViewModel.fetchTvShows().collectLatest {
                tvShowsAdapter.submitData(it)
            }
        }
        lifecycleScope.launch {
            tvShowsViewModel.searchedTvShows?.collectLatest {
                tvShowsAdapter.submitData(it)
            }
        }
    }


    private fun displayProgressBar() {
        tvShowsViewModel.showLoadingBar()
        viewLifecycleOwner.lifecycleScope.launch {
            tvShowsViewModel.showLoadingViewModel.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest {
                val loadingBar = binding.spinKit
                if (it) {
                    loadingBar.visibility = View.VISIBLE
                } else {
                    loadingBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun searchView(){
        with(binding) {
            svTvShow.setOnQueryTextListener(object  : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    svTvShow.clearFocus()
                    tvShowsViewModel.searchByQuery(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    tvShowsViewModel.searchByQuery(newText)
                    return false
                }

            })
        }
    }

    fun goToDetailsFragment(){
        tvShowsAdapter.onItemClick = { currentItem ->
            val action = TvShowsFragmentDirections.actionTvShowsFragmentToTvShowDetails(currentItem)
            findNavController().navigate(action)
        }
    }

}