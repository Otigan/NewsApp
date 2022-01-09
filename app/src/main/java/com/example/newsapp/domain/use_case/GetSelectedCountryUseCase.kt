package com.example.newsapp.domain.use_case

import com.example.newsapp.data.local.repository.CountryDataStoreRepository
import javax.inject.Inject

class GetSelectedCountryUseCase @Inject constructor(
    private val countryDataStoreRepository: CountryDataStoreRepository
) {
    operator fun invoke() = countryDataStoreRepository.countryPreferencesFlow
}