package com.example.newsappcleanarchitecture.feature.newsfeed.presentation.viewmodel

import com.example.newsappcleanarchitecture.CoroutineTestRule
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.domainClass.News
import com.example.newsappcleanarchitecture.feature.newsfeed.domain.usecase.GetNewsFeedUseCase
import com.example.newsappcleanarchitecture.feature.newsfeed.domain.usecase.RefreshNewsUseCase
import com.example.newsappcleanarchitecture.utils.TestNewsFactory
import com.example.newsappcleanarchitecture.utils.network.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class NewsFeedViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule(testDispatcher)

    private var getNewsFeedsUseCase: GetNewsFeedUseCase = mockk()
    private var refreshNewsUseCase: RefreshNewsUseCase = mockk()

    private lateinit var viewModel: NewsFeedViewModel

    @Test
    fun `Given successful resource When view model initiate Then update UI state`() = runTest {
        //Given
        val newsFeeds = listOf(
            TestNewsFactory.createNews("1")
        )
        val newsFeedsFlow = flowOf(Resource.Success(newsFeeds))
        coEvery { getNewsFeedsUseCase() } returns newsFeedsFlow

        //When
        viewModel = NewsFeedViewModel(getNewsFeedsUseCase, refreshNewsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        //Then
        assertEquals(newsFeeds, viewModel.newsState.value)
        assertFalse(viewModel.loadingState.value)
        assertNull(viewModel.errorState.value)
    }

    @Test
    fun `Given failed resource When view model initiate Then update UI state`() = runTest {
        //Given
        val newsFeedsFlow = flowOf(Resource.Failure(false, null, "Error in view model"))
        coEvery { getNewsFeedsUseCase() } returns newsFeedsFlow

        //When
        viewModel = NewsFeedViewModel(getNewsFeedsUseCase, refreshNewsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        //Then
        assertEquals(emptyList<News>(), viewModel.newsState.value)
        assertFalse(viewModel.loadingState.value)
        assertNotNull(viewModel.errorState.value)
    }

    @Test
    fun `Given success resource When view model refreshNewsFeeds Then update UI state`() =
        runTest {
            //Given
            val newsFeeds = listOf(
                TestNewsFactory.createNews("1")
            )
            val newsFeedsFlow = flowOf(Resource.Success(newsFeeds))
            coEvery { getNewsFeedsUseCase() } returns newsFeedsFlow
            coEvery { refreshNewsUseCase() } returns Resource.Success(Unit)

            //When
            viewModel = NewsFeedViewModel(getNewsFeedsUseCase, refreshNewsUseCase)
            viewModel.refreshNews()
            testDispatcher.scheduler.advanceUntilIdle()


            //Then
            assertFalse(viewModel.loadingState.value)
            assertNull(viewModel.errorState.value)
        }

    @Test
    fun `Given failed resource When view model refreshNewsFeeds Then update UI state`() = runTest {
        //Given

        val newsFeedsFlow = flowOf(Resource.Failure(false, null, "Error in view model"))
        coEvery { getNewsFeedsUseCase() } returns newsFeedsFlow
        coEvery { refreshNewsUseCase() } returns Resource.Failure(
            false,
            null,
            "Error in view model"
        )

        //When
        viewModel = NewsFeedViewModel(getNewsFeedsUseCase, refreshNewsUseCase)
        viewModel.refreshNews()
        testDispatcher.scheduler.advanceUntilIdle()

        //Then
        assertFalse(viewModel.loadingState.value)
        assertNotNull(viewModel.errorState.value)
    }
}