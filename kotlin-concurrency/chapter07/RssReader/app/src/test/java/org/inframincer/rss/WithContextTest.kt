package org.inframincer.rss

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import java.util.concurrent.Executors

class WithContextTest {

    @Test
    fun withContextText1() {
        runBlocking {
            val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
            val name = GlobalScope.async(dispatcher) {
                "Susan Calvin"
            }.await()
            println("User: $name")
        }
    }

    @Test
    fun withContextText2() {
        runBlocking {
            val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
            val name = withContext(dispatcher) {
                "Susan Calvin"
            }
            println("User: $name")
        }
    }
}
