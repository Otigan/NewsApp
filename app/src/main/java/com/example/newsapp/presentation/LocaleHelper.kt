package com.example.newsapp.presentation

import android.content.Context
import com.example.newsapp.domain.use_case.LanguageAttachUseCase
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LocaleHelper @Inject constructor(private val languageAttachUseCase: LanguageAttachUseCase) {

    fun onAttach(context: Context) = runBlocking {
        languageAttachUseCase(context)
    }

}