package com.example.newsapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.local.model.RemoteKeys
import com.example.newsapp.data.remote.model.ArticleDto

@Database(entities = [ArticleDto::class, RemoteKeys::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}