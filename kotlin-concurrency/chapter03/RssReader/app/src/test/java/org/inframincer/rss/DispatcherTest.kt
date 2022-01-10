package org.inframincer.rss

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DispatcherTest {

    @Test
    fun dispatcherTest1() {
        runBlocking {
            val task = launch {
                printCurrentThread()
            }
            task.join()
        }
    }

    @Test
    fun dispatcherTest2() {
        val dispatcher = newSingleThreadContext(name = "ServiceCall")
        runBlocking {
            val task = GlobalScope.launch(dispatcher) {
                printCurrentThread()
            }
            task.join()
        }
    }

    private fun printCurrentThread() {
        println("Running in thread [${Thread.currentThread().name}]")
    }
}
