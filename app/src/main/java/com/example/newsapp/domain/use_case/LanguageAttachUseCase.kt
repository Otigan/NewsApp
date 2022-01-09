package com.example.newsapp.domain.use_case

import android.content.Context
import com.example.newsapp.data.local.repository.LanguageDataStoreRepository
import javax.inject.Inject

class LanguageAttachUseCase @Inject constructor(
    private val languageDataStoreRepository: LanguageDataStoreRepository
) {
    suspend operator fun invoke(context: Context) =
        languageDataStoreRepository.onAttach(context)
}