package com.example.newsapp.di

import android.app.Application
import androidx.room.Room
import com.example.newsapp.data.NewsDatabase
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
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(app, NewsDatabase::class.java, "news_db").build()


}