package com.example.newsappcleanarchitecture.feature.newsfeed.data.repository

import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.domainClass.News
import com.example.newsappcleanarchitecture.utils.network.Resource
import kotlinx.coroutines.flow.Flow

interface NewsFeedRepository {
    fun getLatestNews(): Flow<Resource<List<News>>>

    suspend fun refreshLatestNews(): Resource<Unit>
}