package com.example.movieapplication.ui.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapplication.Resource
import com.example.movieapplication.model.TvShow
import com.example.movieapplication.model.TvShowItem
import com.example.movieapplication.network.TvShowApi
import com.example.movieapplication.paging.TvShowsPagingSource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class TvShowRepository @Inject constructor(private var apiService: TvShowApi){
    private var _showLoading = MutableStateFlow(false)
    val showLoading: MutableStateFlow<Boolean> get() = _showLoading

    suspend fun getSimilarTvShows(id : String): Flow<Resource<TvShow>> {
        return flow {
            emit(handleResponse {
                apiService.getSimilarTvs(id)
            })
        }.flowOn(IO)
    }


    fun getPagingSearchedTvs(query : String): Flow<PagingData<TvShowItem>> {
        return Pager(PagingConfig(pageSize = 1)) {
                TvShowsPagingSource(apiService, true, query)
            }.flow
    }

    val data = Pager(PagingConfig(pageSize = 1)) {
        TvShowsPagingSource(apiService)

    }.flow

    private suspend fun <T> handleResponse(apiCall: suspend() -> Response<T>): Resource<T> {
        _showLoading.value = true
        try {
            val response = apiCall()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                _showLoading.value = false
                return Resource.Success(body)
            }
            return Resource.Error(response.errorBody().toString())

        }catch (e: Exception) {
            return Resource.Error("exception")
        }
    }
}