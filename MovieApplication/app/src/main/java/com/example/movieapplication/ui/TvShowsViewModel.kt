package com.example.movieapplication.ui

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapplication.model.TvShowItem
import com.example.movieapplication.network.TvShowApi
import com.example.movieapplication.ui.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel  @Inject constructor(private var tvShowService: TvShowRepository): ViewModel() {
    private val _tvShows = MutableStateFlow(listOf<TvShowItem>())
    val tvShows: MutableStateFlow<List<TvShowItem>> get() = _tvShows

    private var _showLoadingViewModelState = MutableStateFlow(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    private var job: Job? = null

    fun showLoadingBar() {
        viewModelScope.launch {
            tvShowService.showLoading.collectLatest {
                _showLoadingViewModelState.value = it
            }
        }
    }

    fun getTvShows() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tvShowService.getTvShows().collectLatest {
                    if (it.data != null) {
                        _tvShows.value = it.data.results!!

                    }
                }
            }
        }

    }

    fun searchByQuery(query : String?)
    {
        if(query!!.isEmpty()){
            getTvShows()
        } else {
            job?.cancel()
            job = viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    tvShowService.getSearchedTvs(query!!).collectLatest {
                        if (it.data != null) {
                            _tvShows.value = it.data.results!!

                        }
                    }
                }
            }
        }
    }
}