package com.example.newsapp.data.remote.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {


    val languagePreferencesFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_PREFERENCE] ?: DEFAULT_LANGUAGE
    }


    suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_PREFERENCE] = language
        }
    }


    companion object {
        val LANGUAGE_PREFERENCE = stringPreferencesKey("language")
        const val DEFAULT_LANGUAGE = "ru"
    }

}