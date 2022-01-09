package com.example.newsapp.di

import com.example.newsapp.presentation.LocaleHelper
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleHelperEntryPoint {
    val localeHelper: LocaleHelper
}