package com.example.newsapp.data.remote.repository

import com.example.newsapp.data.local.datasource.SearchQueryDataSource
import javax.inject.Inject

class SearchQueryRepository @Inject constructor(private val searchQueryDataSource: SearchQueryDataSource) {

    val searchQueryFlow = searchQueryDataSource.searchQueryFlow

    suspend fun setSearchQuery(newQuery: String) = searchQueryDataSource.setSearchQuery(newQuery)

}