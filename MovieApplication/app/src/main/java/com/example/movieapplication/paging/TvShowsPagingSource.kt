package com.example.movieapplication.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapplication.model.TvShow
import com.example.movieapplication.model.TvShowItem
import com.example.movieapplication.network.TvShowApi
import retrofit2.Response


class TvShowsPagingSource(
    private val apiService: TvShowApi,
    private val searchData: Boolean = false,
    private val query: String = ""
) : PagingSource<Int, TvShowItem>() {

    private  lateinit var response : Response<TvShow>
    override fun getRefreshKey(state: PagingState<Int, TvShowItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>):
            LoadResult<Int, TvShowItem> {

        return try {
            val currentPage = params.key ?: 1

            if(searchData){
                 response = apiService.getSearchedTvs(currentPage,query)
            } else {
                 response = apiService.getTvShows(currentPage)
            }
            val responseData = mutableListOf<TvShowItem>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}