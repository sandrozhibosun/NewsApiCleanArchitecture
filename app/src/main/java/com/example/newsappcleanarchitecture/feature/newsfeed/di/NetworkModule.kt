package com.example.newsappcleanarchitecture.feature.newsfeed.di

import com.example.newsappcleanarchitecture.feature.newsfeed.data.source.remote.NewsFeedService
import com.example.newsappcleanarchitecture.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor(): Interceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader(Constants.AUTHORIZATION, Constants.TOKEN) // weird cuz no bearer token
            .build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authorizationInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES).addInterceptor(httpLoggingInterceptor)
                .addInterceptor(authorizationInterceptor)
        }.build()

    @Provides
    @Singleton
    fun provideNewsFeedService(okHttpClient: OkHttpClient): NewsFeedService {
        val gson = GsonBuilder().create()
         val retrofit = Retrofit.Builder()
            .baseUrl(Constants.NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        return retrofit.create(NewsFeedService::class.java)
    }

}