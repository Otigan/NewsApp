package com.example.newsapp.domain.repository

import androidx.paging.PagingData
import com.example.newsapp.data.remote.model.ArticleDto
import kotlinx.coroutines.flow.Flow

interface HeadlinesRepository {

    fun getHeadlines(country: String): Flow<PagingData<ArticleDto>>

}