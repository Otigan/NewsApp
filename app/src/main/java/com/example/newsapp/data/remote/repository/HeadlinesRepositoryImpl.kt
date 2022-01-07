package com.example.newsapp.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.local.ArticleRemoteMediator
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.remote.api.NewsAPI
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.domain.repository.HeadlinesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HeadlinesRepositoryImpl @Inject constructor(
    private val newsAPI: NewsAPI,
    private val newsDatabase: NewsDatabase
) :
    HeadlinesRepository {

    @ExperimentalPagingApi
    override fun getHeadlines(country: String): Flow<PagingData<ArticleDto>> {

        val pagingSourceFactory = { newsDatabase.articleDao().getAllArticles() }

        return Pager(
            config = PagingConfig(
                pageSize = 30,
                maxSize = 100,
                enablePlaceholders = false
            ),
            remoteMediator = ArticleRemoteMediator(
                newsAPI,
                newsDatabase,
                country
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}

