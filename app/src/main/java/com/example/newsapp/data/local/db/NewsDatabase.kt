package com.example.newsapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.remote.models.Article
import com.example.newsapp.data.remote.models.RemoteKeys

@Database(entities = [Article::class, RemoteKeys::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}