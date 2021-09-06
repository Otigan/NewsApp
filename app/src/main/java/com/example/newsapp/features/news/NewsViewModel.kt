package com.example.newsapp.features.news

import android.content.Context
import androidx.lifecycle.MutableLiveData
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
) : ViewModel() {


    private val currentQuery = MutableLiveData<String>(DEFAULT_QUERY)


    fun getHeadlines(query: String) {
        currentQuery.value = query
    }

    val allNews = currentQuery.switchMap {
        repo.getNews(it).cachedIn(viewModelScope)

    }

    companion object {
        private const val DEFAULT_QUERY = "cats"
    }

}