package com.example.newsapp.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.remote.api.NewsAPI
import com.example.newsapp.data.remote.datasource.SearchNewsPagingSource
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.domain.repository.SearchNewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNewsRepositoryImpl @Inject constructor(private val newsAPI: NewsAPI) :
    SearchNewsRepository {

    override fun searchedNews(query: String, language: String): Flow<PagingData<ArticleDto>> =
        Pager(
            PagingConfig(
                pageSize = 30,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchNewsPagingSource(query, newsAPI, language) }
        ).flow

}