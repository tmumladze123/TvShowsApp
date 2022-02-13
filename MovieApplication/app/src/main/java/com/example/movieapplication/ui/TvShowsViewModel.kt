package com.example.movieapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapplication.model.TvShowItem
import com.example.movieapplication.ui.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel  @Inject constructor(private var tvShowService: TvShowRepository): ViewModel() {

    private var _showLoadingViewModelState = MutableStateFlow(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState


    fun fetchTvShows(): Flow<PagingData<TvShowItem>> {
        return tvShowService.data.cachedIn(viewModelScope)
    }

    fun searchByQuery(query : String): Flow<PagingData<TvShowItem>> {
        return if(query.isEmpty()){
            tvShowService.data.cachedIn(viewModelScope)
        } else {
            tvShowService.getPagingSearchedTvs(query).cachedIn(viewModelScope)
        }
    }
    fun showLoadingBar() {
        viewModelScope.launch {
            tvShowService.showLoading.collectLatest {
                _showLoadingViewModelState.value = it
            }
        }
    }



}