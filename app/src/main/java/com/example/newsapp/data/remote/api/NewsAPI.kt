package com.example.newsapp.data.remote.api

import com.example.newsapp.data.remote.model.NewsResponse
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
        @Query("pageSize") pageSize: Int,
        @Query("language") lang: String
    ): NewsResponse

    @GET("top-headlines")
    suspend fun topHeadlines(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponse


}