package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.domain.use_case.GetSearchQueryUseCase
import com.example.newsapp.domain.use_case.GetSelectedLanguageUseCase
import com.example.newsapp.domain.use_case.SearchNewsUseCase
import com.example.newsapp.domain.use_case.SetSearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val getSelectedLanguageUseCase: GetSelectedLanguageUseCase,
    private val setSearchQueryUseCase: SetSearchQueryUseCase,
    private val getSearchQueryUseCase: GetSearchQueryUseCase
) : ViewModel() {

    private val _articles = MutableStateFlow<PagingData<ArticleDto>>(PagingData.empty())
    val articles = _articles.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchQueryUseCase().collectLatest { newQuery ->
                getSelectedLanguageUseCase().collectLatest { language ->
                    searchNewsUseCase(newQuery, language).cachedIn(viewModelScope)
                        .collectLatest { data ->
                            _articles.value = data
                        }
                }
            }
        }
    }

    fun submitSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setSearchQueryUseCase(query)
        }
    }
}