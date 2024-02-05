package com.example.newsappcleanarchitecture.feature.newsfeed.data.source.remote

import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.responseClass.LatestNewsResponse
import com.example.newsappcleanarchitecture.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface NewsFeedService {

    /**
     *
    Retrieves the latest news articles.
     */
    @GET(Constants.LATEST_NEWS)
    suspend fun getLatestNews(): Response<LatestNewsResponse>
}