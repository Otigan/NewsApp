package com.example.newsapp.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchQueryDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {

    val searchQueryFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[SEARCH_QUERY_PREFERENCE] ?: DEFAULT_SEARCH_QUERY
    }

    suspend fun setSearchQuery(newQuery: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_QUERY_PREFERENCE] = newQuery
        }

    }

    companion object {
        const val DEFAULT_SEARCH_QUERY = "cats"
        val SEARCH_QUERY_PREFERENCE = stringPreferencesKey("search_query")
    }
}