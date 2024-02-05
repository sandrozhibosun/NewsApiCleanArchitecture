package com.example.newsappcleanarchitecture.utils

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

// is optimized to perform CPU-intensive work outside of the main thread. e.g. sorting a list and parsing JSON. If you don't specify a dispatcher, Dispatchers.Default is used
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

// used only for interacting with the UI and performing quick work, e.g. calling suspend functions, running Android UI framework operations, and updating LiveData objects.
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class UnconfinedDispatcher