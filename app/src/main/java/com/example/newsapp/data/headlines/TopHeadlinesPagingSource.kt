package com.example.newsapp.data.headlines

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.api.Articles
import com.example.newsapp.api.NewsAPI
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import javax.inject.Inject


class TopHeadlinesPagingSource @Inject constructor(private val api: NewsAPI) :
    PagingSource<Int, Articles>() {

    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)

        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {

        val position = params.key ?: 1

        return try {
            val country = Locale.getDefault().country.toString()
            val response = api.topHeadlines(country, position, params.loadSize)

            val headlines = response.articles

            LoadResult.Page(
                data = headlines,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (headlines.isEmpty()) null else position + 1,
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }

    }
}