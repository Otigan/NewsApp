package com.example.newsapp.data.headlines

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.preference.PreferenceManager
import com.example.newsapp.api.Articles
import com.example.newsapp.api.NewsAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class TopHeadlinesPagingSource @Inject constructor(
    private val api: NewsAPI,
    @ApplicationContext private val context: Context
) :
    PagingSource<Int, Articles>() {

    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)

        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {

        val position = params.key ?: 1
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        val country = sharedPreferences.getString("country", "")

        return try {

            val response = api.topHeadlines(country!!, position, params.loadSize)

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