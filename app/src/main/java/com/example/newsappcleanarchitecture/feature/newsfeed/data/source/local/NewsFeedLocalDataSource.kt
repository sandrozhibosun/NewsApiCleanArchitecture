package com.example.newsappcleanarchitecture.feature.newsfeed.data.source.local

import com.example.newsappcleanarchitecture.core.data.NewsDatabase
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.entityClass.NewsEntity
import javax.inject.Inject

class NewsFeedLocalDataSource @Inject constructor(
    private val database: NewsDatabase) {

    fun getLatestNews() = database.getNewsDao().getAllNews()

    suspend fun cleanAndSaveNews(news: List<NewsEntity>) = database.getNewsDao().cleanAndCacheNew(news)
}