package com.example.newsappcleanarchitecture.feature.newsfeed.domain.usecase

import com.example.newsappcleanarchitecture.feature.newsfeed.data.repository.NewsFeedRepository
import javax.inject.Inject

class GetNewsFeedUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    operator fun invoke() = newsFeedRepository.getLatestNews()
}