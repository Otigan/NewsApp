package com.example.newsapp.domain.use_case

import com.example.newsapp.data.remote.repository.LanguageDataStoreRepository
import javax.inject.Inject

class GetSelectedLanguageUseCase @Inject constructor(
    private val languageDataStoreRepository: LanguageDataStoreRepository
) {
    operator fun invoke() = languageDataStoreRepository.languagePreferencesFlow
}