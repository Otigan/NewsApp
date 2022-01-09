package com.example.newsapp.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CountryDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {


    val countryPreferencesFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[COUNTRY_PREFERENCE] ?: DEFAULT_COUNTRY
    }

    suspend fun setCountry(country: String) {
        dataStore.edit { preferences ->
            preferences[COUNTRY_PREFERENCE] = country
        }
    }

    companion object {
        val COUNTRY_PREFERENCE = stringPreferencesKey("country")
        const val DEFAULT_COUNTRY = "ru"
    }

}

