package com.example.newsapp.data.news

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.api.Articles
import com.example.newsapp.api.NewsAPI
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class NewsPagingSource(
    private val newsAPI: NewsAPI,
    private val query: String
) : PagingSource<Int, Articles>() {

    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {
        val position = params.key ?: 1

        val lang = Locale.getDefault().language.toString()

        return try {
            //val response = newsAPI.topHeadlines(query, position, params.loadSize)

            val response = newsAPI.everything(query, position, params.loadSize, lang)
            val news = response.articles

            LoadResult.Page(
                data = news,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (news.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}