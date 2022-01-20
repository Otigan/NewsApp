package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import com.example.newsapp.util.NetworkStatusHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

sealed class State {
    object Fetched : State()
    object Error : State()
}

@ExperimentalCoroutinesApi
@HiltViewModel
class NetworkStatusViewModel @Inject constructor(private val networkStatusHelper: NetworkStatusHelper) :
    ViewModel() {

    val networkState = networkStatusHelper

}