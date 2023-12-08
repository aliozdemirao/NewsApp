package com.aliozdemir.newsapp.di

import android.app.Application
import com.aliozdemir.newsapp.data.manger.LocalUserMangerImpl
import com.aliozdemir.newsapp.data.remote.NewsApi
import com.aliozdemir.newsapp.data.repository.NewsRepositoryImpl
import com.aliozdemir.newsapp.domain.manager.LocalUserManger
import com.aliozdemir.newsapp.domain.repository.NewsRepository
import com.aliozdemir.newsapp.domain.usecases.app_entry.AppEntryUseCases
import com.aliozdemir.newsapp.domain.usecases.app_entry.ReadAppEntry
import com.aliozdemir.newsapp.domain.usecases.app_entry.SaveAppEntry
import com.aliozdemir.newsapp.domain.usecases.news.GetNews
import com.aliozdemir.newsapp.domain.usecases.news.NewsUseCases
import com.aliozdemir.newsapp.domain.usecases.news.SearchNews
import com.aliozdemir.newsapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserMangerImpl(context = application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )

    @Provides
    @Singleton
    fun provideApiInstance(): NewsApi {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi
    ): NewsRepository {
        return NewsRepositoryImpl(newsApi)
    }

    @Provides
    @Singleton
    fun provideNewsUseCases(
        newsRepository: NewsRepository
    ): NewsUseCases {
        return NewsUseCases(
            getNews = GetNews(newsRepository),
            searchNews = SearchNews(newsRepository)
        )
    }

}