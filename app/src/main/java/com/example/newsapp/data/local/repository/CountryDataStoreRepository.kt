package com.example.newsapp.data.local.repository

import com.example.newsapp.data.local.datasource.CountryDataSource
import javax.inject.Inject

class CountryDataStoreRepository @Inject constructor(private val countryDataSource: CountryDataSource) {

    suspend fun setCountry(country: String) = countryDataSource.setCountry(country)

    val countryPreferencesFlow = countryDataSource.countryPreferencesFlow

}