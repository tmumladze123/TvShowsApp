package com.example.movieapplication.network

import com.example.movieapplication.GetResponseUrl.APIKEY
import com.example.movieapplication.GetResponseUrl.TV_SHOW_SERVICE
import com.example.movieapplication.model.TvShow
import com.example.movieapplication.model.TvShowItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowApi {
    @GET(TV_SHOW_SERVICE)
    suspend fun getTvShows(
        @Query("page") page :Int = 1,
        @Query("api_key") api_key :String = APIKEY
    ): Response<TvShow>

    @GET("tv/{tvID}/similar?api_key=9b4d6d06951d3d7549d41ba439c620a4")
    suspend fun getSimilarTvs(
        @Path("tvID") id: String
    ): Response<TvShow>
    @GET("search/tv")
    suspend fun getSearchedTvs(
        @Query("page") page :Int,
        @Query("query") query :String,
        @Query("api_key") api_key :String = APIKEY
    ): Response<TvShow>

}

