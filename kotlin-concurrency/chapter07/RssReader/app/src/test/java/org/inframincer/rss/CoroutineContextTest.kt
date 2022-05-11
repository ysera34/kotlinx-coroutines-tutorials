package org.inframincer.rss

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.Executors

class CoroutineContextTest {

    @Test
    fun dispatcherDefaultTest() {
        runBlocking {
            GlobalScope.launch(Dispatchers.Default) {
                println("CurrentThread Name is ${Thread.currentThread().name}")
            }
        }
        GlobalScope.launch(context = Dispatchers.Default) {
            println("CurrentThread Name is ${Thread.currentThread().name}")
        }

        GlobalScope.launch {
            println("CurrentThread Name is ${Thread.currentThread().name}")
        }
    }

    @Test
    fun dispatcherUnconfinedTest() {
        runBlocking {
            GlobalScope.launch(Dispatchers.Unconfined) {
                println("Starting in ${Thread.currentThread().name}")
                delay(500)
                println("Resuming in ${Thread.currentThread().name}")
            }.join()
        }

        runBlocking {
            GlobalScope.launch(Dispatchers.Unconfined) {
                println("CurrentThread Name is ${Thread.currentThread().name}")
                println("CurrentThread Name is ${Thread.currentThread().name}")
            }
            delay(500)
        }
    }

    @Test
    fun singleThreadContextTest() {
        val obsoleteDispatcher = newSingleThreadContext("myThread")
        runBlocking {
            GlobalScope.launch(obsoleteDispatcher) {
                println("Starting in ${Thread.currentThread().name}")
                delay(500)
                println("Resuming in ${Thread.currentThread().name}")
            }.join()
        }
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        runBlocking {
            GlobalScope.launch(dispatcher) {
                println("Starting in ${Thread.currentThread().name}")
                delay(500)
                println("Resuming in ${Thread.currentThread().name}")
            }.join()
        }
    }

    @Test
    fun fixedThreadPoolContextTest() {
        val obsoleteDispatcher = newFixedThreadPoolContext(4, "myThreadPool")
        runBlocking {
            GlobalScope.launch(obsoleteDispatcher) {
                println("Starting in ${Thread.currentThread().name}")
                delay(500)
                println("Resuming in ${Thread.currentThread().name}")
            }.join()
        }
        val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking {
            GlobalScope.launch(dispatcher) {
                println("Starting in ${Thread.currentThread().name}")
                delay(500)
                println("Resuming in ${Thread.currentThread().name}")
            }.join()
        }
    }
}
