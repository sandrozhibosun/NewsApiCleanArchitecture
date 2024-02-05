package com.example.newsappcleanarchitecture.feature.newsfeed.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.entityClass.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newsEntities: List<NewsEntity>)

    @Query("DELETE FROM news")
    fun deleteAll()

    @Transaction
    suspend fun cleanAndCacheNew(businesses: List<NewsEntity>) {
        deleteAll()
        insert(businesses)
    }
}