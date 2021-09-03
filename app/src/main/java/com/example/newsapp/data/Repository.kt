package com.example.newsapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.newsapp.api.NewsAPI
import com.example.newsapp.data.headlines.TopHeadlinesPagingSource
import com.example.newsapp.data.news.NewsPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val newsAPI: NewsAPI) {

    fun getHeadlines() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TopHeadlinesPagingSource(newsAPI) }
    ).liveData

    fun getNews(query: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NewsPagingSource(newsAPI, query) }
    ).liveData
}