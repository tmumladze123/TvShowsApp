package com.example.movieapplication.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapplication.model.TvShowItem
import com.example.movieapplication.ui.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(private var tvShowService: TvShowRepository) : ViewModel() {
    private val _similarTvShows = MutableStateFlow(listOf<TvShowItem>())
    val similarTvShows: MutableStateFlow<List<TvShowItem>> get() = _similarTvShows

    private var _showLoadingViewModelState = MutableStateFlow(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    fun showLoadingBar() {
        viewModelScope.launch {
            tvShowService.showLoading.collectLatest {
                _showLoadingViewModelState.value = it
            }
        }
    }

    fun getSimilarTvShows(id : String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tvShowService.getSimilarTvShows(id).collectLatest {
                    if (it.data != null) {
                        _similarTvShows.value = it.data.results!!
                    }
                }
            }
        }
    }
}