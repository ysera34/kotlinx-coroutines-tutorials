package org.inframincer.rss

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.Executors

class CoroutineContextCompositionTest {

    /**
     * Running in pool-1-thread-1 @coroutine#2
     * Error captured
     * Message : An operation is not implemented: Not implemented!
     */
    @Test
    fun coroutineContextCompositionTest1() {
        runBlocking {
            val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
            val handler = CoroutineExceptionHandler { _, throwable ->
                println("Error captured")
                println("Message : ${throwable.message}")
            }

            GlobalScope.launch(dispatcher + handler) {
                println("Running in ${Thread.currentThread().name}")
                TODO("Not implemented!")
            }.join()
        }
    }

    /**
     * Running in DefaultDispatcher-worker-1 @coroutine#2
     * Error captured
     * Message : An operation is not implemented: Not implemented!
     */
    @Test
    fun coroutineContextCompositionTest2() {
        runBlocking {
            val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
            val handler = CoroutineExceptionHandler { _, throwable ->
                println("Error captured")
                println("Message : ${throwable.message}")
            }
            val context = dispatcher + handler
            val temporalContext = context.minusKey(dispatcher.key)

            GlobalScope.launch(temporalContext) {
                println("Running in ${Thread.currentThread().name}")
                TODO("Not implemented!")
            }.join()
        }
    }
}
