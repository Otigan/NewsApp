package com.example.newsapp.di

import android.app.Application
import androidx.room.Room
import com.example.newsapp.db.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NewsDatabase =
        Room.databaseBuilder(app, NewsDatabase::class.java, "news_db").build()

}