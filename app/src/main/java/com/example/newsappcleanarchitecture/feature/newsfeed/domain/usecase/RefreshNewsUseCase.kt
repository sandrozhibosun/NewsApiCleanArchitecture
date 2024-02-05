package com.example.newsappcleanarchitecture.feature.newsfeed.domain.usecase

import com.example.newsappcleanarchitecture.feature.newsfeed.data.repository.NewsFeedRepository
import javax.inject.Inject

class RefreshNewsUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    suspend operator fun invoke() = newsFeedRepository.refreshLatestNews()
}