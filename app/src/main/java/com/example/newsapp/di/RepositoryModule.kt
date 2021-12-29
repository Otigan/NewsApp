package com.example.newsapp.di

import com.example.newsapp.data.remote.repository.HeadlinesRepositoryImpl
import com.example.newsapp.domain.repository.HeadlinesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHeadlinesRepository(impl: HeadlinesRepositoryImpl): HeadlinesRepository

}