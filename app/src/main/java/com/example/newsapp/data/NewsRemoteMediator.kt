package com.example.newsapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.api.NewsAPI
import com.example.newsapp.db.NewsDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.RemoteKeys
import retrofit2.HttpException
import java.io.IOException

const val BREAKING_NEWS_CONST = 1
const val COMMON_NEWS_CONST = 2
const val LIKED_NEWS_CONST = 3

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val query: String? = null,
    private val api: NewsAPI,
    private val db: NewsDatabase,
    private val newsType: Int
) : RemoteMediator<Int, Article>() {

    override suspend fun initialize(): InitializeAction {

        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.

        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Log.d("page", " refresh: ${remoteKeys?.nextKey?.minus(1)}")
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.

                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                Log.d("page", " prepend: $prevKey")
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.

                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                Log.d("page", " append: $nextKey")
                nextKey
            }
        }

        try {

            val type = NewsType.create(newsType)

            val apiResponse = when (type) {
                NewsType.BREAKING -> {
                    api.topHeadlines("ru", page, 20)
                }
                NewsType.COMMON -> {
                    api.everything(query!!, page, 20, "ru")
                }
            }


            val articles = apiResponse.articles

            if (type == NewsType.BREAKING) {
                for (article in articles) {
                    article.isBreakingNews = true
                }
            }

            val endOfPaginationReached = articles.isEmpty()

            db.withTransaction {
                val oldArticles = db.getNewsDao().getList()
                val liked = ArrayList<String>()
                for (article in oldArticles) {
                    if (article.isLiked == true) {
                        liked.add(article.url)
                    }
                }

                for (article in articles) {
                    if (article.url in liked) {
                        article.isLiked = true
                    }
                }

                if (loadType == LoadType.REFRESH) {
                    when (type) {
                        NewsType.BREAKING -> {
                            db.getNewsDao().deleteAllBreakingNews()
                            db.getRemoteKeysDao().deleteBreakingRemoteKeys()
                        }
                        NewsType.COMMON -> {
                            db.getNewsDao().deleteAll()
                            db.getRemoteKeysDao().clearRemoteKeys()
                        }
                    }
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKeys(
                        url = it.url,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        isBreaking = type == NewsType.BREAKING,
                        isLiked = it.isLiked
                    )
                }
                db.getRemoteKeysDao().insertAll(keys)
                db.getNewsDao().insertAll(articles)

            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { url ->
                db.getRemoteKeysDao().remoteKeysUrl(url)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { article ->
                // Get the remote keys of the first items retrieved
                db.getRemoteKeysDao().remoteKeysUrl(article.url)
            }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { article ->
                // Get the remote keys of the last item retrieved
                db.getRemoteKeysDao().remoteKeysUrl(article.url)
            }
    }

}

enum class NewsType(val value: Int) {
    BREAKING(BREAKING_NEWS_CONST),
    COMMON(COMMON_NEWS_CONST);

    companion object {
        fun create(x: Int): NewsType {
            return when (x) {
                1 -> BREAKING
                2 -> COMMON
                else -> throw IllegalStateException()
            }
        }
    }
}

