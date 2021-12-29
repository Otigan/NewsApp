package com.example.newsapp.domain.use_case

import com.example.newsapp.domain.repository.HeadlinesRepository
import javax.inject.Inject

class GetHeadlinesUseCase @Inject constructor(private val headlinesRepository: HeadlinesRepository) {


    operator fun invoke() =
        headlinesRepository.getHeadlines()

}