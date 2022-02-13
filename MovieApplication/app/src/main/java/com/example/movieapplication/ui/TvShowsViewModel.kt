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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel  @Inject constructor(private var tvShowService: TvShowRepository): ViewModel() {
    private var _searchedTvShows : Flow<PagingData<TvShowItem>>? = null
    val searchedTvShows: Flow<PagingData<TvShowItem>>? get() = _searchedTvShows

    private var _showLoadingViewModelState = MutableStateFlow(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    private var job: Job? = null

    fun fetchTvShows(): Flow<PagingData<TvShowItem>> {
        return tvShowService.Data.cachedIn(viewModelScope)
    }

    fun showLoadingBar() {
        viewModelScope.launch {
            tvShowService.showLoading.collectLatest {
                _showLoadingViewModelState.value = it
            }
        }
    }


    fun searchByQuery(query : String?)
    {
        if(query!!.isEmpty()){
            fetchTvShows()
        } else {
            job?.cancel()
            job = viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    tvShowService.getPagingSearchedTvs(query)
                    tvShowService.searchedData?.collectLatest {
                        _searchedTvShows = tvShowService.searchedData
                    }
                    }
                }
            }
    }

}