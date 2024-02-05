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

class NewsFeedRepositoryImpl @Inject constructor(
    private val newsFeedRemoteDataSource: NewsFeedRemoteDataSource,
    private val newsFeedLocalDataSource: NewsFeedLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NewsFeedRepository {

    override fun getLatestNews(): Flow<Resource<List<News>>> {
        return getNewsCacheFirst()
    }

    private fun getNewsCacheFirst(): Flow<Resource<List<News>>> {
        return newsFeedLocalDataSource.getLatestNews().map { news ->
            if (news.isNotEmpty()) {
                Resource.Success(news.map { entity ->
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
        }.catch { e -> Resource.Failure(false, null, e.message) }
            .flowOn(ioDispatcher)
    }

    private suspend fun getCurrentLocalData(): Resource<List<News>> {
        return newsFeedLocalDataSource.getLatestNews().first().let { newsList ->
            if (newsList.isNotEmpty()) {
                Resource.Success(newsList.map { entity ->
                    entity.toDomain()
                })
            } else {
                Resource.Failure(false, null, "No news found")
            }
        }
    }

    override suspend fun refreshLatestNews(): Resource<Unit> {
        return withContext(ioDispatcher) {
            try {
                withTimeout(5000L) {
                    when (val resource = newsFeedRemoteDataSource.getLatestNews()) {
                        is Resource.Success -> {
                            newsFeedLocalDataSource.cleanAndSaveNews(resource.value.news.map { response ->
                                response.toEntity()
                            })
                            Resource.Success(Unit)
                        }

                        is Resource.Failure -> {
                            resource
                        }

                        else -> Resource.Failure(false, null, "Unknown error")
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Resource.Failure(false, null, "Operation timed out")
            } catch (e: Exception) {
                Resource.Failure(false, null, e.message)
            }
        }
    }
}