package com.example.newsappcleanarchitecture.feature.newsfeed.domain.usecase

import com.example.newsappcleanarchitecture.feature.newsfeed.data.repository.NewsFeedRepository
import javax.inject.Inject

class GetNewsFeedUseCase @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository
) {
    /**
     * Invokes the use case to fetch the latest news feed.
     *
     * This function is marked with the 'operator' keyword to allow it to be
     * called as if it were a function, improving its readability when used.
     *
     * @return A flow of [Resource] representing the state and data of the latest news feed request.
     */
    operator fun invoke() = newsFeedRepository.getLatestNews()
}