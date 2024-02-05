package com.example.newsappcleanarchitecture.feature.newsfeed.data.model.entityClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val id: String,
    val author: String,
    // Serialize the List<String> to a JSON String
    val category: List<String>,
    val description: String,
    val image: String,
    val language: String,
    val published: String,
    val title: String,
    val url: String
)