package com.example.newsapp.data.news

import android.content.Context
import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.api.Articles
import com.example.newsapp.api.NewsAPI
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import java.io.IOException
import java.util.*


class NewsPagingSource(
    private val newsAPI: NewsAPI,
    private val query: String?,
    private val newsType: Int,
    @ApplicationContext private val context: Context

) : PagingSource<Int, Articles>() {


    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface NewsPagingSourceEntryPoint {
        fun sharedPref(): SharedPreferences
    }

    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {
        val position = params.key ?: 1

        val lang = Locale.getDefault().language.toString()

        val entryPoint =
            EntryPointAccessors.fromApplication(
                context,
                NewsPagingSourceEntryPoint::class.java
            )

        val sharedPreferences = entryPoint.sharedPref()

        val country = sharedPreferences.getString("country", "")

        return try {
            val response = when (newsType) {
                NewsType.Common.value -> newsAPI.everything(
                    query!!,
                    position,
                    params.loadSize,
                    lang
                )
                NewsType.TopHeadline.value -> newsAPI.topHeadlines(
                    country!!,
                    position,
                    params.loadSize
                )
                else -> throw IllegalStateException()
            }

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

enum class NewsType(val value: Int) {
    TopHeadline(1),
    Common(2),
}