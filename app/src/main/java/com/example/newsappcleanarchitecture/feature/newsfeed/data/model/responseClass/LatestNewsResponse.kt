package com.example.newsappcleanarchitecture.feature.newsfeed.data.model.responseClass

data class LatestNewsResponse(
    val news: List<NewsResponse>,
    val page: Int,
    val status: String
)

data class NewsResponse(
    val author: String,
    val category: List<String>,
    val description: String,
    val id: String,
    val image: String,
    val language: String,
    val published: String,
    val title: String,
    val url: String
)