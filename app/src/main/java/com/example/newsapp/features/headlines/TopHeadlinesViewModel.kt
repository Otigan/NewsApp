package com.example.newsapp.features.headlines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    fun headlines() = repo.getHeadlines().cachedIn(viewModelScope)

    private val currentCountry = MutableLiveData<String>(Locale.getDefault().country.toString())


}