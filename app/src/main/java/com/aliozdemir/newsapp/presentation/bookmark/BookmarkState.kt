package com.aliozdemir.newsapp.presentation.bookmark

import com.aliozdemir.newsapp.domain.model.Article

data class BookmarkState(
    val articles: List<Article> = emptyList()
)