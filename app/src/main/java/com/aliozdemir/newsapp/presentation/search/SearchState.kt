package com.aliozdemir.newsapp.presentation.search

import androidx.paging.PagingData
import com.aliozdemir.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

data class SearchState(
    val searchQuery: String = "",
    val articles: Flow<PagingData<Article>>? = null
)