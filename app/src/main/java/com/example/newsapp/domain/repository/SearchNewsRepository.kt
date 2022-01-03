package com.example.newsapp.domain.repository

import androidx.paging.PagingData
import com.example.newsapp.data.remote.model.ArticleDto
import kotlinx.coroutines.flow.Flow

interface SearchNewsRepository {

    fun searchedNews(query: String): Flow<PagingData<ArticleDto>>

}