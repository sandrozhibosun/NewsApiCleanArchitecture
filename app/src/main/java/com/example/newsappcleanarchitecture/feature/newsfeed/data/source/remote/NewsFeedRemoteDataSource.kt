package com.example.newsappcleanarchitecture.feature.newsfeed.data.source.remote

import com.example.newsappcleanarchitecture.utils.network.toResource
import javax.inject.Inject

class NewsFeedRemoteDataSource @Inject constructor(private val newsFeedService: NewsFeedService) {

    suspend fun getLatestNews() = newsFeedService.getLatestNews().toResource()
}