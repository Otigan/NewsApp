package com.example.newsapp.data.local.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bumptech.glide.load.HttpException
import com.example.newsapp.data.local.db.NewsDatabase
import com.example.newsapp.data.local.model.RemoteKeys
import com.example.newsapp.data.remote.api.NewsAPI
import com.example.newsapp.data.remote.model.ArticleDto
import java.io.IOException
import java.net.ConnectException

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
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val newsResponse =
                newsAPI.topHeadlines(country, page, state.config.pageSize)

            val articles = newsResponse.articles
            val endOfPaginationReached = articles.isEmpty()

            newsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDatabase.articleDao().deleteAll()
                    newsDatabase.remoteKeysDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKeys(
                        articleUrl = it.url,
                        prevKey = prevKey,
                        nextKey = nextKey,
                    )
                }
                newsDatabase.remoteKeysDao().insertAll(keys)
                newsDatabase.articleDao().insertAll(articles)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        } catch (exception: ConnectException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ArticleDto>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { url ->
                newsDatabase.remoteKeysDao().remoteKeysArticleUrl(url)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleDto>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { article ->
                newsDatabase.remoteKeysDao().remoteKeysArticleUrl(article.url)
            }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleDto>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { article ->
                newsDatabase.remoteKeysDao().remoteKeysArticleUrl(article.url)
            }
    }
}
