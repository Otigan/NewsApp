package com.example.newsapp.domain.use_case

import com.example.newsapp.domain.repository.SearchNewsRepository
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(private val searchNewsRepository: SearchNewsRepository) {


    operator fun invoke(query: String, language: String) =
        searchNewsRepository.searchedNews(query, language)

}