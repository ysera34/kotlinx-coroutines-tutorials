package org.inframincer.rss

import android.os.Bundle
import android.provider.Contacts
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.inframincer.rss.adapter.ArticleAdapter
import org.inframincer.rss.search.ResultsCounter
import org.inframincer.rss.search.Searcher

class SearchActivity : AppCompatActivity() {

    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: ArticleAdapter
    private val searcher = Searcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ArticleAdapter()
        articlesRecyclerView = findViewById<RecyclerView>(R.id.articles).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        findViewById<Button>(R.id.searchButton).setOnClickListener {
            viewAdapter.clear()
            GlobalScope.launch {
                ResultsCounter.reset()
                search()
            }
        }

        GlobalScope.launch {
            updateCounter()
        }
    }

    private suspend fun search() {
        val query = findViewById<EditText>(R.id.searchEditText).text.toString()
        val channel = searcher.search(query)

        while (!channel.isClosedForReceive) {
            val article = channel.receive()

            GlobalScope.launch(Dispatchers.Main) {
                viewAdapter.add(article)
            }
        }
    }

    private suspend fun updateCounter() {
        val notifications = ResultsCounter.getNotificationChannel()
        val resultsTextView = findViewById<TextView>(R.id.resultsTextView)

        while (!notifications.isClosedForReceive) {
            val newAmount = notifications.receive()

            withContext(Dispatchers.Main) {
                resultsTextView.text = "Results: $newAmount"
            }
        }
    }
}
