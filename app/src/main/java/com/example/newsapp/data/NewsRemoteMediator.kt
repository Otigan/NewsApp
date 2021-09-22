package com.example.newsapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.api.Article
import com.example.newsapp.api.NewsAPI
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class NewsRemoteMediator(
    private val query: String,
    private val database: NewsDatabase,
    private val api: NewsAPI
) : RemoteMediator<Int, Article>() {

    private val newsDao = database.newsDao()
    private val remoteDao = database.remoteKeysDao()

    /*override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)
        return if (System.currentTimeMillis() - database.() >= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }*/

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        return try {

            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.next?.minus(1) ?: 1
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)

                    val nextKey = remoteKeys?.next
                    if (nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    nextKey
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prev
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    prevKey
                }
            }

            val response = api.topHeadlines("us", page, state.config.pageSize)

            val endOfPagination = response.articles.size < state.config.pageSize

            val prev = if (page == 1) null else page - 1
            val next = if (endOfPagination) null else page + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteDao.clearRemoteKeys()
                    newsDao.deleteAll()
                }

                remoteDao.insert(
                    NewsRemoteKeys(query, prev, next)
                )

                newsDao.insertAll(response.articles)
            }
            MediatorResult.Success(
                endOfPaginationReached = next == null
            )

        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>
    ): NewsRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.author?.let { author ->
                remoteDao.remoteKeysRepoId(author)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): NewsRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                remoteDao.remoteKeysRepoId(it.author)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): NewsRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                remoteDao.remoteKeysRepoId(it.author)
            }
    }


}