package com.aliozdemir.newsapp.di

import android.app.Application
import androidx.room.Room
import com.aliozdemir.newsapp.data.local.NewsDao
import com.aliozdemir.newsapp.data.local.NewsDatabase
import com.aliozdemir.newsapp.data.local.NewsTypeConvertor
import com.aliozdemir.newsapp.data.manger.LocalUserMangerImpl
import com.aliozdemir.newsapp.data.remote.NewsApi
import com.aliozdemir.newsapp.data.repository.NewsRepositoryImpl
import com.aliozdemir.newsapp.domain.manager.LocalUserManger
import com.aliozdemir.newsapp.domain.repository.NewsRepository
import com.aliozdemir.newsapp.domain.usecases.app_entry.AppEntryUseCases
import com.aliozdemir.newsapp.domain.usecases.app_entry.ReadAppEntry
import com.aliozdemir.newsapp.domain.usecases.app_entry.SaveAppEntry
import com.aliozdemir.newsapp.domain.usecases.news.DeleteArticle
import com.aliozdemir.newsapp.domain.usecases.news.GetArticle
import com.aliozdemir.newsapp.domain.usecases.news.GetArticles
import com.aliozdemir.newsapp.domain.usecases.news.GetNews
import com.aliozdemir.newsapp.domain.usecases.news.NewsUseCases
import com.aliozdemir.newsapp.domain.usecases.news.SearchNews
import com.aliozdemir.newsapp.domain.usecases.news.UpsertArticle
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
        localUserManger: LocalUserManger,
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger),
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
        newsApi: NewsApi,
        newsDao: NewsDao
    ): NewsRepository {
        return NewsRepositoryImpl(newsApi, newsDao)
    }

    @Provides
    @Singleton
    fun provideNewsUseCases(
        newsRepository: NewsRepository,
        newsDao: NewsDao
    ): NewsUseCases {
        return NewsUseCases(
            getNews = GetNews(newsRepository),
            searchNews = SearchNews(newsRepository),
            upsertArticle = UpsertArticle(newsDao),
            deleteArticle = DeleteArticle(newsDao),
            getArticles = GetArticles(newsDao),
            getArticle = GetArticle(newsDao)
        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(
        application: Application
    ): NewsDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = NewsDatabase::class.java,
            name = "news_db"
        ).addTypeConverter(NewsTypeConvertor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(
        newsDatabase: NewsDatabase
    ): NewsDao = newsDatabase.newsDao

}