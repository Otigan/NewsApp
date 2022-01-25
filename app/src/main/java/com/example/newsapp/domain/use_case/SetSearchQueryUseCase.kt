package com.example.newsapp.domain.use_case

import com.example.newsapp.data.remote.repository.SearchQueryRepository
import javax.inject.Inject

class SetSearchQueryUseCase @Inject constructor(private val searchQueryRepository: SearchQueryRepository) {

    suspend operator fun invoke(newQuery: String) = searchQueryRepository.setSearchQuery(newQuery)
}