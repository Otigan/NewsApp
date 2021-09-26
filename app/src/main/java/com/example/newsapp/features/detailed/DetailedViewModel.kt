package com.example.newsapp.features.detailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailedViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    fun setLike(url: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.setLike(url)
    }


}