package com.example.newsapp.domain.repository

import androidx.paging.PagingData
import com.example.newsapp.data.remote.model.Article
import kotlinx.coroutines.flow.Flow

interface HeadlinesRepository {

    fun getHeadlines(): Flow<PagingData<Article>>

}