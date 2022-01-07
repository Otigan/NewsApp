package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.domain.use_case.GetHeadlinesUseCase
import com.example.newsapp.domain.use_case.GetSelectedCountryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val getHeadlinesUseCase: GetHeadlinesUseCase,
    private val getSelectedCountryUseCase: GetSelectedCountryUseCase,
) :
    ViewModel() {


    private val _articles = MutableStateFlow<PagingData<ArticleDto>>(PagingData.empty())
    val articles = _articles.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getSelectedCountryUseCase().collectLatest { country ->
                getHeadlinesUseCase(country).cachedIn(viewModelScope).collectLatest { data ->
                    _articles.value = data
                }
            }
        }
    }
}
