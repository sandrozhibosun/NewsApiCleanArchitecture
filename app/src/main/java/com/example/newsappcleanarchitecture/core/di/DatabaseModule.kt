package com.example.newsappcleanarchitecture.core.di

import android.content.Context
import androidx.room.Room
import com.example.newsappcleanarchitecture.core.data.NewsDatabase
import com.example.newsappcleanarchitecture.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NewsDatabase::class.java,
        Constants.ROOMDB_DBNAME
    )
        .build()
}