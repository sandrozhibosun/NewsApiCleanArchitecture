package com.example.newsappcleanarchitecture.feature.newsfeed.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.domainClass.News
import com.example.newsappcleanarchitecture.feature.newsfeed.domain.usecase.GetNewsFeedUseCase
import com.example.newsappcleanarchitecture.feature.newsfeed.domain.usecase.RefreshNewsUseCase
import com.example.newsappcleanarchitecture.utils.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val getNewsFeedUseCase: GetNewsFeedUseCase,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow<List<News>>(emptyList())
    val newsState = _newsState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            _errorState.value = throwable.message
        }
    }

    init {
        getLatestNews()
    }


    /**
     * Retrieves the latest news using the [getNewsFeedUseCase].
     *
     * Observes the resource emitted by the use case to update the UI state accordingly,
     * including loading indicators and handling success or failure states.
     */
    private fun getLatestNews() {
        getNewsFeedUseCase().onStart { _loadingState.value = true }
            .catch { e -> _errorState.value = e.message }
            .onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _loadingState.value = false
                        _newsState.value = resource.value
                    }

                    is Resource.Failure -> {
                        _loadingState.value = false
                        _errorState.value = resource.errorDescription
                    }

                    else -> {
                        _loadingState.value = true
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Initiates a refresh of the news data via the [refreshNewsUseCase].
     *
     * This function is typically called by user interaction to refresh the data displayed.
     * It updates the loading state and handles the result of the refresh operation,
     * updating the UI state as necessary.
     */
    fun refreshNews() {
        viewModelScope.launch(exceptionHandler) {
            _loadingState.value = true
            when (val resource = refreshNewsUseCase()) {
                is Resource.Success -> {
                    _loadingState.value = false
                }

                is Resource.Failure -> {
                    _loadingState.value = false
                    _errorState.value = resource.errorDescription
                }

                else -> {}
            }
        }
    }

}