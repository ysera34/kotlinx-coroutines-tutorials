package org.inframincer.rss

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.inframincer.rss.adapter.ArticleAdapter
import org.inframincer.rss.adapter.ArticleLoader
import org.inframincer.rss.producer.ArticleProducer

class MainActivity : AppCompatActivity(), ArticleLoader {

    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ArticleAdapter()
        articlesRecyclerView = findViewById<RecyclerView>(R.id.articles).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        GlobalScope.launch {
            loadArticles()
        }
    }

    override suspend fun loadArticles() {
        val producer = ArticleProducer.producer

        if (!producer.isClosedForReceive) {
            val articles = producer.receive()

            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "loadArticles()", Toast.LENGTH_SHORT).show()
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                viewAdapter.addAll(articles)
            }
        }
    }
}
