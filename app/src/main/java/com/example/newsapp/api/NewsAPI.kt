package com.example.newsapp.api

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsAPI {

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = BuildConfig.API_KEY
    }

    @Headers("Authorization: $API_KEY")
    @GET("top-headlines")
    suspend fun topHeadlines(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("pageSize") perPage: Int
    ): ApiResponse

    @Headers("Authorization: $API_KEY")
    @GET("everything")
    suspend fun everything(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") perPage: Int
    ): ApiResponse

}