package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.domain.use_case.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(private val searchNewsUseCase: SearchNewsUseCase) :
    ViewModel() {

    private val currentQuery = MutableStateFlow<String?>(null)

    @ExperimentalCoroutinesApi
    val searchResults = currentQuery.flatMapLatest { query ->
        query?.let {
            searchNewsUseCase(query)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    fun submitSearchQuery(query: String) {
        currentQuery.value = query
    }
}