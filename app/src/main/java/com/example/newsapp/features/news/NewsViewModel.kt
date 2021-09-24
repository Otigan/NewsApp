package com.example.newsapp.features.news

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repo: Repository,
    state: SavedStateHandle
) : ViewModel() {


    val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)


    fun changeQuery(query: String) {
        currentQuery.value = query
    }

    val allNews = currentQuery.switchMap {
        repo.getNews(it).cachedIn(viewModelScope)

    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "cats"
    }

}