package com.example.newsapp.presentation.breaking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.example.newsapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    @ExperimentalPagingApi
    fun getBreakingNews() = repo.getBreakingNews().cachedIn(viewModelScope)

}