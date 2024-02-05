package com.example.newsappcleanarchitecture.feature.newsfeed.data.repository

import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.domainClass.News
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.toDomain
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.toEntity
import com.example.newsappcleanarchitecture.feature.newsfeed.data.source.local.NewsFeedLocalDataSource
import com.example.newsappcleanarchitecture.feature.newsfeed.data.source.remote.NewsFeedRemoteDataSource
import com.example.newsappcleanarchitecture.utils.IoDispatcher
import com.example.newsappcleanarchitecture.utils.network.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

/**
 * Implementation of the NewsFeedRepository interface, providing functionality
 * to fetch the latest news either from a local data source or a remote data source.
 */
class NewsFeedRepositoryImpl @Inject constructor(
    private val newsFeedRemoteDataSource: NewsFeedRemoteDataSource,
    private val newsFeedLocalDataSource: NewsFeedLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NewsFeedRepository {

    /**
     * Fetches the latest news, preferring data from the local cache first. If the cache
     * is empty or outdated, it attempts to fetch fresh data from the remote data source
     * and updates the local cache.
     */
    override fun getLatestNews(): Flow<Resource<List<News>>> {
        return getNewsCacheFirst()
    }

    /**
     * Private helper method that implements the cache-first strategy for fetching news.
     * It first checks the local data source for cached news data and emits it if available.
     * If the cache is empty, it then fetches data from the remote source and updates the cache.
     */
    private fun getNewsCacheFirst(): Flow<Resource<List<News>>> {
        return newsFeedLocalDataSource.getLatestNews().map { newsEntities ->
            if (newsEntities.isNotEmpty()) {
                Resource.Success(newsEntities.map { entity ->
                    entity.toDomain()
                })
            } else {
                when (val refreshResult = refreshLatestNews()) {
                    is Resource.Success -> {
                        getCurrentLocalData()
                    }

                    is Resource.Failure -> {
                        refreshResult
                    }

                    else -> Resource.Failure(false, null, "Unknown error")
                }
            }
        }.catch { e -> Resource.Failure(false, null, e.message) }.flowOn(ioDispatcher)
    }

    /**
     * Fetches the current local data from the local data source and wraps it in a Resource.
     * Used internally to emit the latest cached data after refreshing the cache.
     */
    private suspend fun getCurrentLocalData(): Resource<List<News>> {
        return newsFeedLocalDataSource.getLatestNews().first().let { newsEntities ->
            if (newsEntities.isNotEmpty()) {
                Resource.Success(newsEntities.map { entity ->
                    entity.toDomain()
                })
            } else {
                Resource.Failure(false, null, "No news found")
            }
        }
    }

    /**
     * Refreshes the latest news by fetching it from the remote data source and updating
     * the local cache. This method is typically called when the cached data is outdated
     * or upon user request to refresh the news feed.
     */
    override suspend fun refreshLatestNews(): Resource<Unit> {
        return withContext(ioDispatcher) {
            try {
                when (val resource = newsFeedRemoteDataSource.getLatestNews()) {
                    is Resource.Success -> {
                        val newsResponseList = resource.value.news
                        newsFeedLocalDataSource.cleanAndSaveNews(newsResponseList.map { response ->
                            response.toEntity()
                        })
                        Resource.Success(Unit)
                    }

                    is Resource.Failure -> {
                        resource
                    }

                    else -> Resource.Failure(false, null, "Unknown error")
                }
            } catch (e: Exception) {
                Resource.Failure(false, null, e.message)
            }
        }
    }
}