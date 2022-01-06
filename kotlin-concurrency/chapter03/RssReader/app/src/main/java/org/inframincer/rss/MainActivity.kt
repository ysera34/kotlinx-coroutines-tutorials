package org.inframincer.rss

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

class MainActivity : AppCompatActivity() {

    private val dispatcher = newFixedThreadPoolContext(2, "IO")
    private val factory = DocumentBuilderFactory.newInstance()

    private val feeds = listOf(
        "https://www.npr.org/rss/rss.php?id=1001",
        "http://rss.cnn.com/rss/cnn_topstories.rss",
        "http://feeds.foxnews.com/foxnews/politics?format=xml",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        asyncLoadNews1()
        asyncLoadNews2()
    }

    private fun asyncLoadNews1() = GlobalScope.launch {
        val requests = mutableListOf<Deferred<List<String>>>()
        feeds.mapTo(requests) {
            asyncFetchHeadlines(it, dispatcher)
        }

        requests.forEach {
            it.await()
        }
        val headlines = requests.flatMap {
            it.getCompleted()
        }

        val newsCount = findViewById<TextView>(R.id.news1Count)
        GlobalScope.launch(Dispatchers.Main) {
            newsCount.text = "Found ${headlines.size} News in ${requests.size} feeds"
        }
    }

    private fun asyncLoadNews2() = GlobalScope.launch {
        val requests = mutableListOf<Deferred<List<String>>>()
        feeds.toMutableList().plusElement("htt:myNewsFeed").mapTo(requests) {
            asyncFetchHeadlines(it, dispatcher)
        }

        requests.forEach {
            it.join()
        }
        val headlines = requests
            .filterNot { it.isCancelled }
            .flatMap {
                it.getCompleted()
            }
        val failedFeeds = requests
            .filter { it.isCancelled }

        val newsCountTextView = findViewById<TextView>(R.id.news2Count)
        val news2FailedFeedTextView = findViewById<TextView>(R.id.news2FailedFeed)
        GlobalScope.launch(Dispatchers.Main) {
            newsCountTextView.text = "Found ${headlines.size} News in ${requests.size} feeds"

            if (failedFeeds.isNotEmpty()) {
                news2FailedFeedTextView.text = "Failed to fetch ${failedFeeds.size} feeds"
            }
        }
    }

    private fun asyncFetchHeadlines(feed: String, dispatcher: CoroutineDispatcher) =
        GlobalScope.async(dispatcher) {
            val builder = factory.newDocumentBuilder()
            val xml = builder.parse(feed)
            val news = xml.getElementsByTagName("channel").item(0)
            (0 until news.childNodes.length)
                .map { news.childNodes.item(it) }
                .filter { Node.ELEMENT_NODE == it.nodeType }
                .map { it as Element }
                .filter { "item" == it.tagName }
                .map { it.getElementsByTagName("title").item(0).textContent }
        }
}
