package com.example.newsapp.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.Article

@Dao
interface NewsDao {

    @Query("SELECT * FROM articles")
    suspend fun getList(): List<Article>

    @Query("SELECT * FROM articles WHERE isLiked = 1 ORDER BY title ASC")
    suspend fun getListOfLiked(): List<Article>

    // Common news

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Query("SELECT * FROM articles WHERE isBreakingNews IS null")
    fun getAll(): PagingSource<Int, Article>

    @Query("DELETE FROM articles WHERE isBreakingNews IS null")
    suspend fun deleteAll()


    // Breaking news

    @Query("SELECT * FROM articles WHERE isBreakingNews = 1")
    fun getAllBreakingNews(): PagingSource<Int, Article>

    @Query("DELETE FROM articles WHERE isBreakingNews = 1")
    suspend fun deleteAllBreakingNews()

    // Like

    @Query("UPDATE articles SET isLiked = 1 WHERE url LIKE:url")
    suspend fun setLike(url: String)

    @Query("UPDATE articles SET isLiked = 0 WHERE url LIKE:url")
    suspend fun removeLike(url: String)

    @Query("SELECT * from articles WHERE isLiked = 1 ORDER BY title asc")
    fun getAllLikedArticles(): PagingSource<Int, Article>
}