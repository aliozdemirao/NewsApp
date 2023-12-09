package com.aliozdemir.newsapp.domain.usecases.news

import com.aliozdemir.newsapp.data.local.NewsDao
import com.aliozdemir.newsapp.domain.model.Article

class DeleteArticle(
    private val newsDao: NewsDao
) {

    suspend operator fun invoke(article: Article) {
        newsDao.delete(article = article)
    }

}