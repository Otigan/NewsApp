package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.domain.use_case.GetSelectedLanguageUseCase
import com.example.newsapp.domain.use_case.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val getSelectedLanguageUseCase: GetSelectedLanguageUseCase
) :
    ViewModel() {

    private val currentQuery = MutableStateFlow<String?>(null)
    private lateinit var selectedLanguage: String

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getSelectedLanguageUseCase().collect { language ->
                selectedLanguage = language
                currentQuery.value = "cats"
            }
        }
    }

    @ExperimentalCoroutinesApi
    val searchResults = currentQuery.flatMapLatest { query ->
        query?.let {
            searchNewsUseCase(query, selectedLanguage)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    fun submitSearchQuery(query: String) {
        currentQuery.value = query
    }
}