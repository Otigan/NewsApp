package com.example.newsapp.domain.use_case

import com.example.newsapp.data.remote.repository.SearchQueryRepository
import javax.inject.Inject

class GetSearchQueryUseCase @Inject constructor(private val searchQueryRepository: SearchQueryRepository) {

    operator fun invoke() = searchQueryRepository.searchQueryFlow

}