package com.example.newsappcleanarchitecture.feature.newsfeed.data.model

import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.domainClass.News
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.entityClass.NewsEntity
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.responseClass.NewsResponse

fun NewsResponse.toEntity(): NewsEntity {
    return NewsEntity(
        id = this.id,
        author = this.author,
        category = this.category, // Convert List<String> to JSON String
        description = this.description,
        image = this.image,
        language = this.language,
        published = this.published,
        title = this.title,
        url = this.url
    )
}

fun NewsEntity.toDomain(): News {
    return News(
        id = this.id,
        author = this.author,
        category = this.category, // Convert JSON String to List<String>
        description = this.description,
        image = this.image,
        language = this.language,
        published = this.published,
        title = this.title,
        url = this.url
    )
}