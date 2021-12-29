package com.example.newsapp.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.newsapp.data.NewsPagingSource
import com.example.newsapp.data.remote.api.NewsAPI
import com.example.newsapp.domain.repository.HeadlinesRepository
import javax.inject.Inject

class HeadlinesRepositoryImpl @Inject constructor(
    private val newsAPI: NewsAPI
) :
    HeadlinesRepository {

    override fun getHeadlines() =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(newsAPI) }
        ).flow

}