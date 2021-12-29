package com.example.newsapp.domain.datasource

import com.example.newsapp.data.remote.model.ApiResponse

interface HeadlinesDataSource {

    suspend fun getHeadlines(): ApiResponse

}