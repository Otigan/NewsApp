package com.example.newsapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.newsapp.api.NewsAPI
import com.example.newsapp.data.local.db.NewsDatabase
import com.example.newsapp.data.remote.models.Article
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val newsAPI: NewsAPI,
    private val db: NewsDatabase
) {


    fun getCachedNews(query: String): LiveData<PagingData<Article>> {
        val pagingSourceFactory = { db.getNewsDao().getAll() }

        val dbQuery = "%${query.replace(' ', '%')}%"

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = NewsRemoteMediator(
                dbQuery,
                newsAPI,
                db,
                COMMON_NEWS_CONST
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    @ExperimentalPagingApi
    fun getBreakingNews(): LiveData<PagingData<Article>> {
        val pagingSourceFactory = { db.getNewsDao().getAllBreakingNews() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = NewsRemoteMediator(
                api = newsAPI,
                db = db,
                newsType = BREAKING_NEWS_CONST
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    fun getLikedNews(): LiveData<PagingData<Article>> {

        val pagingSourceFactory = { db.getNewsDao().getAllLikedArticles() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    suspend fun setLike(url: String) = db.getNewsDao().setLike(url)

    suspend fun removeLike(url: String) = db.getNewsDao().removeLike(url)

    suspend fun getListOfLiked(): List<Article> = db.getNewsDao().getListOfLiked()


}