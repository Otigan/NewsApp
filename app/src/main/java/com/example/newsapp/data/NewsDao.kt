package com.example.newsapp.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.api.Article

@Dao
interface NewsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(articles: List<Article>)

    @Query("DELETE FROM articles")
    fun deleteAll()

    @Query("SELECT * FROM articles")
    fun pagingSource(): PagingSource<Int, Article>

}