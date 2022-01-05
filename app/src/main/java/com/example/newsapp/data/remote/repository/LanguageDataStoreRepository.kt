package com.example.newsapp.data.remote.repository

import com.example.newsapp.data.remote.datasource.LanguageDataSource
import javax.inject.Inject

class LanguageDataStoreRepository @Inject constructor(
    private val languageDataSource: LanguageDataSource
) {

    val languagePreferencesFlow = languageDataSource.languagePreferencesFlow

    suspend fun setLanguage(language: String) = languageDataSource.setLanguage(language)

}