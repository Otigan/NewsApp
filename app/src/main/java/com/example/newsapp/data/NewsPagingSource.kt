package com.example.newsapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.api.NewsAPI
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(
    private val newsAPI: NewsAPI,
    private val query: String
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        TODO("NOT YET IMPLEMENTED")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: 1

        return try {
            val response = newsAPI.headlines(query, position, params.loadSize)
            val news = response.articles

            LoadResult.Page(
                data = news,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (news.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

}