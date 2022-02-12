package com.example.movieapplication.ui.details

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.BaseFragment
import com.example.movieapplication.GetResponseUrl.IMAGEPATH
import com.example.movieapplication.databinding.TvShowDetailsFragmentBinding
import com.example.movieapplication.extensions.setImage
import com.example.movieapplication.model.TvShowItem
import com.example.movieapplication.ui.TvShowsFragmentDirections
import com.example.movieapplication.ui.adapters.SimilarTvShowsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvShowDetailsFragment : BaseFragment<TvShowDetailsFragmentBinding>(TvShowDetailsFragmentBinding :: inflate) {

    private val tvShowsDetailsViewModel: TvShowDetailsViewModel by viewModels()
    private val args: TvShowDetailsFragmentArgs by navArgs()
    private lateinit var similarTvShowsAdapter: SimilarTvShowsAdapter
    private lateinit var currentTvShowItem:  TvShowItem
    override fun start() {
        currentTvShowItem = args.tvShowItem
        tvShowsDetailsViewModel.getSimilarTvShows(currentTvShowItem.id.toString())
        initRecycler()
        setData()
        observer()
        changeSimilarTvData()
        displayProgressBar()
    }
    private fun displayProgressBar() {
        tvShowsDetailsViewModel.showLoadingBar()
        viewLifecycleOwner.lifecycleScope.launch {
            tvShowsDetailsViewModel.showLoadingViewModel.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest {
                val loadingBar = binding.spinKit
                if (it) {
                    loadingBar.visibility = View.VISIBLE
                } else {
                    loadingBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun changeSimilarTvData(){
        similarTvShowsAdapter.onItemClick = { currentItem ->
            currentTvShowItem = currentItem
            tvShowsDetailsViewModel.getSimilarTvShows(currentTvShowItem.id.toString())
            setData()
        }
    }

    fun initRecycler(){
        similarTvShowsAdapter = SimilarTvShowsAdapter()
        binding.rvSimilarTvShows.adapter = similarTvShowsAdapter
        binding.rvSimilarTvShows.layoutManager = LinearLayoutManager( requireContext(), LinearLayoutManager.HORIZONTAL,false)
    }

    fun setData(){
        with(binding) {
            with(currentTvShowItem) {
                tvName.text = name.toString()
                tvDesc.text = overview.toString()
                ivPoster.setImage(IMAGEPATH + posterPath.toString())
            }
        }
    }

    fun observer(){
        viewLifecycleOwner.lifecycleScope.launch {
            tvShowsDetailsViewModel.similarTvShows.collectLatest {
                similarTvShowsAdapter.similarTvShows = it
            }
        }
    }
}