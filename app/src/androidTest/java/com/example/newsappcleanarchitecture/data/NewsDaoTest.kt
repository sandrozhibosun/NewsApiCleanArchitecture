package com.example.newsappcleanarchitecture.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsappcleanarchitecture.core.data.NewsDatabase
import com.example.newsappcleanarchitecture.feature.newsfeed.data.source.local.NewsDao
import com.example.newsappcleanarchitecture.utls.TestNewsFactory
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDaoTest {

    private lateinit var database: NewsDatabase
    private lateinit var newsDao: NewsDao

    @Before
    fun setup() {
        // Create an in-memory version of the Room database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).allowMainThreadQueries().build()

        newsDao = database.getNewsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndReadNews() = runBlocking {
        val newsList = listOf(
            TestNewsFactory.createNewsEntity("1")
        )
        newsDao.insert(newsList)

        val allNews = newsDao.getAllNews().first()
        assertEquals(newsList, allNews)
    }

    @Test
    fun cleanAndCacheNews() = runBlocking {
        val newsList1 = listOf(
            TestNewsFactory.createNewsEntity("1")
        )
        val newsList2 = listOf(
            TestNewsFactory.createNewsEntity("2")
        )

        newsDao.insert(newsList1)
        newsDao.cleanAndCacheNew(newsList2)

        val allNews = newsDao.getAllNews().first()
        assertEquals(newsList2, allNews)
    }

    @Test
    fun deleteAllNews() = runBlocking {
        val newsList = listOf(
            TestNewsFactory.createNewsEntity("1")
        )

        newsDao.insert(newsList)
        newsDao.deleteAll()

        val allNews = newsDao.getAllNews().first()
        TestCase.assertTrue(allNews.isEmpty())
    }
}