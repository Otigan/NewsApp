package com.example.newsapp.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val currentQuery = MutableLiveData<String>(DEFAULT_QUERY)

    val news = currentQuery.switchMap { newQuery ->
        repository.getHeadlines(newQuery).cachedIn(viewModelScope)
    }

    fun searchNews(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "us"
    }

}