package com.example.newsapp.data.local.datasource

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class LanguageDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {


    val languagePreferencesFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_PREFERENCE] ?: DEFAULT_LANGUAGE
    }

    private suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_PREFERENCE] = language
        }
    }

    suspend fun setLocale(context: Context, language: String): Context =
        withContext(Dispatchers.IO) {
            setLanguage(language)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, language)
            } else {
                updateResourceLegacy(context, language)
            }
        }

    suspend fun onAttach(context: Context): Context =
        withContext(Dispatchers.IO) {
            val lang = languagePreferencesFlow.firstOrNull()
            Log.d("LanguageDataSource", "lastLang:$lang ")
            if (lang.isNullOrBlank()) {
                Log.d("LanguageDataSource", "lang: $lang")
                setLocale(context, DEFAULT_LANGUAGE)
            } else {
                Log.d("LanguageDataSource", "lang: $lang")
                setLocale(context, lang)
            }
        }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourceLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources

        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }

    companion object {
        val LANGUAGE_PREFERENCE = stringPreferencesKey("language")
        const val DEFAULT_LANGUAGE = "ru"
    }

}