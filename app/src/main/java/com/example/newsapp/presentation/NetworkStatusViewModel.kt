package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.newsapp.util.NetworkStatusHelper
import com.example.newsapp.util.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

sealed class State {
    object Fetched : State()
    object Error : State()
}

@HiltViewModel
class NetworkStatusViewModel @Inject constructor(private val networkStatusHelper: NetworkStatusHelper) :
    ViewModel() {

    @ExperimentalCoroutinesApi
    val networkState = networkStatusHelper.networkStatus.map(
        onAvailable = { State.Fetched },
        onUnavailable = { State.Error }
    ).asLiveData(Dispatchers.IO)

}