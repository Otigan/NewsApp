package com.example.newsapp.data.local.repository

import android.content.Context
import com.example.newsapp.data.local.datasource.LanguageDataSource
import javax.inject.Inject

class LanguageDataStoreRepository @Inject constructor(
    private val languageDataSource: LanguageDataSource
) {

    val languagePreferencesFlow = languageDataSource.languagePreferencesFlow

    suspend fun onAttach(context: Context) =
        languageDataSource.onAttach(context)

    suspend fun setLocale(context: Context, language: String): Context =
        languageDataSource.setLocale(context, language)

}