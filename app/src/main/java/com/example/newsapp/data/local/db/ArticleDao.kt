package com.example.newsapp.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.remote.model.ArticleDto

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleDto>)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): PagingSource<Int, ArticleDto>

    @Query("DELETE FROM articles")
    suspend fun deleteAll()

}