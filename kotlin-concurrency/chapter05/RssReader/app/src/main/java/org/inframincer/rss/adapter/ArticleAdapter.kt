package org.inframincer.rss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.inframincer.rss.R
import org.inframincer.rss.model.Article

class ArticleAdapter(
    private val loader: ArticleLoader,
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    private val articles = mutableListOf<Article>()
    private var loading = false

    class ViewHolder(
        val layout: LinearLayout,
        val feed: TextView,
        val title: TextView,
        val summary: TextView,
    ) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_layout, parent, false) as LinearLayout
        val feed = layout.findViewById<TextView>(R.id.feed)
        val title = layout.findViewById<TextView>(R.id.title)
        val summary = layout.findViewById<TextView>(R.id.summary)

        return ViewHolder(layout, feed, title, summary)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]

        if (!loading && position >= articles.size - 2) {
            loading = true

            GlobalScope.launch {
                loader.loadArticles()
                loading = false
            }
        }

        holder.feed.text = article.feed
        holder.title.text = article.title
        holder.summary.text = article.summary
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun addAll(articles: List<Article>) {
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }
}
