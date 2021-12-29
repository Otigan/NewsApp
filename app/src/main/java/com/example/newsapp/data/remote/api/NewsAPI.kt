package com.example.newsapp.data.remote.api

import com.example.newsapp.data.remote.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
    }

    @GET("everything")
    suspend fun everything(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") perPage: Int,
        @Query("language") lang: String
    ): ApiResponse

    @GET("top-headlines")
    suspend fun topHeadlines(
        @Query("country") country: String = "us",
        @Query("page") page: Int,
        @Query("pageSize") perPage: Int
    ): ApiResponse


}