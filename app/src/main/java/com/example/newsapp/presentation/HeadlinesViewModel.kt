package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.data.remote.model.Article
import com.example.newsapp.domain.use_case.GetHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


sealed class HeadlinesEvent {
    class Success(val headlines: PagingData<Article>) : HeadlinesEvent()
    class Error(val errorMessage: String) : HeadlinesEvent()
    object Loading : HeadlinesEvent()
}

@HiltViewModel
class HeadlinesViewModel @Inject constructor(private val getHeadlinesUseCase: GetHeadlinesUseCase) :
    ViewModel() {

    val articles = getHeadlinesUseCase().cachedIn(viewModelScope)

    /*private val headlinesChannel = Channel<HeadlinesEvent>(Channel.BUFFERED)
    val headlinesFlow = headlinesChannel.receiveAsFlow()

    init {
        getHeadlines()
    }

    private fun getHeadlines() = viewModelScope.launch(Dispatchers.IO) {
        getHeadlinesUseCase().collect { pagingData ->
            headlinesChannel.send(HeadlinesEvent.Success(pagingData))
        }
    }*/
}