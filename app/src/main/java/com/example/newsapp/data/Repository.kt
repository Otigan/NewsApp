package com.example.newsapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.newsapp.api.NewsAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val newsAPI: NewsAPI,
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences
) {


    fun getCountry() = sharedPreferences.getString("country", "")

    fun getHeadlines() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NewsPagingSource(newsAPI, null, NewsType.TopHeadline(), context) }
    ).liveData

    fun getNews(query: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NewsPagingSource(newsAPI, query, NewsType.Common(), context) }
    ).liveData
}