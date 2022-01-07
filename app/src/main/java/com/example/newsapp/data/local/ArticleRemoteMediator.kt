package com.example.newsapp.data.local

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.data.local.model.RemoteKeys
import com.example.newsapp.data.remote.api.NewsAPI
import com.example.newsapp.data.remote.model.ArticleDto
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val newsAPI: NewsAPI,
    private val newsDatabase: NewsDatabase,
    private val country: String
) : RemoteMediator<Int, ArticleDto>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleDto>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                Log.d("page", " append: $nextKey")
                nextKey

            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                Log.d("page", " prepend: $prevKey")
                prevKey
            }
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Log.d("page", " refresh: ${remoteKeys?.nextKey?.minus(1)}")
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
        }
        try {
            val newsResponse = newsAPI.topHeadlines(country, page, 30)
            val articles = newsResponse.articles
            val endOfPaginationReached = articles.isEmpty()
            newsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDatabase.articleDao().deleteAllArticles()
                    newsDatabase.remoteKeysDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKeys(articleId = it.id, prevKey, nextKey)
                }
                newsDatabase.remoteKeysDao().insertAll(keys)
                newsDatabase.articleDao().insertAllArticles(articles)

            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleDto>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { article ->
                // Get the remote keys of the last item retrieved
                newsDatabase.remoteKeysDao().remoteKeysArticleId(article.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleDto>): RemoteKeys? {

        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { article ->
                // Get the remote keys of the first items retrieved
                newsDatabase.remoteKeysDao().remoteKeysArticleId(article.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ArticleDto>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { articleId ->
                newsDatabase.remoteKeysDao().remoteKeysArticleId(articleId)
            }
        }
    }
}