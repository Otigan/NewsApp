package com.example.newsapp.presentation.favourite

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

    suspend fun listOfLiked() = repo.getListOfLiked()

    fun setLike(url: String) = viewModelScope.launch(Dispatchers.IO) {
        repo.setLike(url)
    }

    val likedNews = repo.getLikedNews().cachedIn(viewModelScope)

}
