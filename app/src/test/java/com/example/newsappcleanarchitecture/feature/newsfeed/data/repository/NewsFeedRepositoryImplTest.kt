package com.example.newsappcleanarchitecture.feature.newsfeed.data.repository

import com.example.newsappcleanarchitecture.CoroutineTestRule
import com.example.newsappcleanarchitecture.feature.newsfeed.data.source.local.NewsFeedLocalDataSource
import com.example.newsappcleanarchitecture.feature.newsfeed.data.source.remote.NewsFeedRemoteDataSource
import com.example.newsappcleanarchitecture.utils.TestNewsFactory
import com.example.newsappcleanarchitecture.utils.network.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsFeedRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule(testDispatcher)

    private lateinit var repository: NewsFeedRepositoryImpl

    private val localDataSource: NewsFeedLocalDataSource = mockk()
    private val remoteDataSource: NewsFeedRemoteDataSource = mockk()

    @Before
    fun setup() {
        repository = NewsFeedRepositoryImpl(remoteDataSource, localDataSource, testDispatcher)
    }

    @Test
    fun `Given given valid cache When getLatestNews Then return successful resource`() = runTest {
        // Given
        val newsList = listOf(
            TestNewsFactory.createNews("1")
        )
        coEvery { localDataSource.getLatestNews() } returns flowOf(
            listOf(
                TestNewsFactory.createNewsEntity(
                    "1"
                )
            )
        )

        // When
        val result = repository.getLatestNews().first()

        // Then
        coVerify(exactly = 0) { remoteDataSource.getLatestNews() }
        assertTrue(result is Resource.Success)
        assertEquals(newsList, (result as Resource.Success).value)
    }

    @Test
    fun `Given Cache is empty When getLatestNews Then fetch data successfully from remote`() =
        runTest {
            // Given
            val newsList = listOf(
                TestNewsFactory.createNews("1")
            )
            coEvery { localDataSource.getLatestNews() } returns flowOf(emptyList())
            coEvery { remoteDataSource.getLatestNews() } returns Resource.Success(
                TestNewsFactory.createLatestNewsResponse(
                    "1"
                )
            )
            coEvery { localDataSource.cleanAndSaveNews(any()) } returns Unit

            // When
            repository.getLatestNews().first()

            // Then
            coVerify(exactly = 1) { remoteDataSource.getLatestNews() }
            coVerify(exactly = 1) {
                localDataSource.cleanAndSaveNews(
                    any()
                )
            }
        }

    @Test
    fun `Given cache is empty When getLatestNews and fetch data failed from remote Then return failed`() =
        runTest {
            // Given
            coEvery { localDataSource.getLatestNews() } returns flowOf(emptyList())
            coEvery { remoteDataSource.getLatestNews() } returns Resource.Failure(
                false,
                null,
                "Unknown error"
            )

            // When
            val result = repository.getLatestNews().first()

            // Then
            coVerify(exactly = 1) { remoteDataSource.getLatestNews() }
            assertTrue(result is Resource.Failure)
        }

    @Test
    fun `Given remote source success Then refreshLatestNews then update cache`() = runTest {
        // Given

        coEvery { remoteDataSource.getLatestNews() } returns Resource.Success(
            TestNewsFactory.createLatestNewsResponse(
                "1"
            )
        )
        coEvery { localDataSource.cleanAndSaveNews(any()) } returns Unit

        // When
        repository.refreshLatestNews()

        // Then
        coVerify(exactly = 1) { localDataSource.cleanAndSaveNews(any()) }
    }

    @Test
    fun `Given remote source failed When refreshLatestNews Then not update cache`() = runTest {
        // Given

        coEvery { remoteDataSource.getLatestNews() } returns Resource.Failure(
            false,
            null,
            "Unknown error"
        )

        // When
        val result = repository.refreshLatestNews()

        // Then
        coVerify(exactly = 0) { localDataSource.cleanAndSaveNews(any()) }
        assertTrue(result is Resource.Failure)
    }
}