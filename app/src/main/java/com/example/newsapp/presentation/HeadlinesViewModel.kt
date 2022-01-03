package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.domain.use_case.GetHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(getHeadlinesUseCase: GetHeadlinesUseCase) :
    ViewModel() {

    val articles = getHeadlinesUseCase().cachedIn(viewModelScope)

}