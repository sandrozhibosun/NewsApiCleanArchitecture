package com.example.newsappcleanarchitecture.utls

import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.domainClass.News
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.entityClass.NewsEntity
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.responseClass.LatestNewsResponse
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.responseClass.NewsResponse

object TestNewsFactory {

    fun createLatestNewsResponse(id: String): LatestNewsResponse {
        return LatestNewsResponse(
            news = listOf(createNewsResponse(id)),
            page = 1,
            status = "ok"
        )
    }
    fun createNewsResponse(id: String): NewsResponse {
        return NewsResponse(
            author = "Author $id",
            category = listOf("Category 1", "Category 2"),
            description = "Description of news item $id",
            id = id,
            image = "https://example.com/image$id.png",
            language = "en",
            published = "2022-01-01",
            title = "Title $id",
            url = "https://example.com/news$id"
        )
    }

    fun createNewsEntity(id: String): NewsEntity {
        return NewsEntity(
            id = id,
            author = "Author $id",
            category = listOf("Category 1", "Category 2"),
            description = "Description of news item $id",
            image = "https://example.com/image$id.png",
            language = "en",
            published = "2022-01-01",
            title = "Title $id",
            url = "https://example.com/news$id"
        )
    }

    fun createNews(id: String): News {
        return News(
            author = "Author $id",
            category = listOf("Category 1", "Category 2"),
            description = "Description of news item $id",
            id = id,
            image = "https://example.com/image$id.png",
            language = "en",
            published = "2022-01-01",
            title = "Title $id",
            url = "https://example.com/news$id"
        )
    }

}