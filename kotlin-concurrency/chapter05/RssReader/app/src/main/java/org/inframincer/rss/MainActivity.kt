package org.inframincer.rss

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import org.inframincer.rss.adapter.ArticleAdapter
import org.inframincer.rss.model.Article
import org.inframincer.rss.model.Feed
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

class MainActivity : AppCompatActivity() {

    private val dispatcher = newFixedThreadPoolContext(2, "IO")
    private val factory = DocumentBuilderFactory.newInstance()
    private val feeds = listOf(
        Feed("npr", "https://www.npr.org/rss/rss.php?id=1001"),
        Feed("cnn", "http://rss.cnn.com/rss/cnn_topstories.rss"),
        Feed("fox", "http://feeds.foxnews.com/foxnews/politics?format=xml"),
        Feed("inv", "htt:myNewsFeed"),
    )

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

        asyncLoadNews()
    }

    private fun asyncLoadNews() = GlobalScope.launch {
        val requests = mutableListOf<Deferred<List<Article>>>()
        feeds.mapTo(requests) {
            asyncFetchArticles(it, dispatcher)
        }

        requests.forEach {
            it.join()
        }
        val articles = requests
            .filterNot { it.isCancelled }
            .flatMap {
                it.getCompleted()
            }
        val failedFeeds = requests
            .filter { it.isCancelled }

        GlobalScope.launch(Dispatchers.Main) {
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            viewAdapter.addAll(articles)
        }
    }

    private fun asyncFetchArticles(feed: Feed, dispatcher: CoroutineDispatcher) = GlobalScope.async(dispatcher) {
        delay(2000)
        val builder = factory.newDocumentBuilder()
        val xml = builder.parse(feed.url)
        val news = xml.getElementsByTagName("channel").item(0)
        (0 until news.childNodes.length)
            .map { news.childNodes.item(it) }
            .filter { Node.ELEMENT_NODE == it.nodeType }
            .map { it as Element }
            .filter { "item" == it.tagName }
            .map {
                val title = it.getElementsByTagName("title").item(0).textContent
                val summary = it.getElementsByTagName("description").item(0).textContent
                Article(feed.name, title, summary)
            }
    }
}
