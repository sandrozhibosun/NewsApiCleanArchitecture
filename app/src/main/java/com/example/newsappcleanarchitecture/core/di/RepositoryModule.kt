package com.example.newsappcleanarchitecture.core.di

import com.example.newsappcleanarchitecture.feature.newsfeed.data.repository.NewsFeedRepository
import com.example.newsappcleanarchitecture.feature.newsfeed.data.repository.NewsFeedRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindNewsRepository(newsRepositoryImpl: NewsFeedRepositoryImpl): NewsFeedRepository
}