package com.example.newsapp.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.local.datasource.ArticleRemoteMediator
import com.example.newsapp.data.local.db.NewsDatabase
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

    @OptIn(ExperimentalPagingApi::class)
    override fun getHeadlines(country: String): Flow<PagingData<ArticleDto>> {

        val pagingSourceFactory = { newsDatabase.articleDao().getAllArticles() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
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

