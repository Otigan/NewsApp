package com.example.newsapp.domain.use_case

import com.example.newsapp.data.remote.repository.LanguageDataStoreRepository
import javax.inject.Inject

class SelectLanguageUseCase @Inject constructor(
    private val languageDataStoreRepository: LanguageDataStoreRepository
) {

    suspend operator fun invoke(language: String) =
        languageDataStoreRepository.setLanguage(language)

}