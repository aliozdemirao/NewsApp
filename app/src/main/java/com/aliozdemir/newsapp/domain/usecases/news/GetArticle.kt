package com.aliozdemir.newsapp.domain.usecases.news

import com.aliozdemir.newsapp.data.local.NewsDao
import com.aliozdemir.newsapp.domain.model.Article

class GetArticle(
    private val newsDao: NewsDao
) {

    suspend operator fun invoke(url: String): Article? {
        return newsDao.getArticle(url = url)
    }

}