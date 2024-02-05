package com.example.newsappcleanarchitecture.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.entityClass.NewsEntity
import com.example.newsappcleanarchitecture.feature.newsfeed.data.source.local.NewsDao

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverter::class)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
}