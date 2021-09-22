package com.example.newsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import java.util.*

@Database(entities = [Currency::class, NewsRemoteKeys::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
    abstract fun remoteKeysDao(): RemoteKeyDao
}