package com.example.newsapp.api

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsAPI {

    companion object {
        const val BASE_URL = "https://newsapi.org/"
        const val KEY = BuildConfig.API_KEY
    }

    @Headers("Authorization: $KEY")
    @GET("v2/top-headlines")
    suspend fun headlines(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): NewsResponse

}