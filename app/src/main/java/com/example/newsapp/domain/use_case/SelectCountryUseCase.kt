package com.example.newsapp.domain.use_case

import com.example.newsapp.data.local.repository.CountryDataStoreRepository
import javax.inject.Inject

class SelectCountryUseCase @Inject constructor(
    private val countryDataStoreRepository: CountryDataStoreRepository
) {

    suspend operator fun invoke(country: String) = countryDataStoreRepository.setCountry(country)

}