package org.inframincer.rss.adapter

interface ArticleLoader {
    suspend fun loadArticles()
}
