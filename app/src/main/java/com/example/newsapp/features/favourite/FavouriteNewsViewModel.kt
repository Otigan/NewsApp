package com.example.newsapp.features.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteNewsViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    fun removeLike(url: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.removeLike(url)
    }

    val likedNews = repo.getLikedNews().cachedIn(viewModelScope)

}
