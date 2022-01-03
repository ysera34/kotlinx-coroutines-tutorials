package org.inframincer.rss

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AsyncCoroutineTest {

    @Test
    fun asyncTest1() {
        runBlocking {
            val task = GlobalScope.async {
                doSomeThing()
            }
            task.join()
            println("Completed")
        }
    }

    @InternalCoroutinesApi
    @Test
    fun asyncTest2() {
        runBlocking {
            val task = GlobalScope.async {
                doSomeThing()
            }
            task.join()
            if (task.isCancelled) {
                val exception = task.getCancellationException()
                println("Error with message : ${exception.cause}")
            } else {
                println("Success")
            }
        }
    }

    @Test
    fun asyncTest3() {
        runBlocking {
            val task = GlobalScope.async {
                doSomeThing()
            }
            delay(2000)
            println("Completed")
        }
    }

    @Test
    fun asyncTest4() {
        runBlocking {
            val task = GlobalScope.async {
                doSomeThing()
            }
            task.await()
            println("Completed")
        }
    }

    @Test
    fun asyncTest5() {
        runBlocking {
            val task = GlobalScope.async {
                doSomeThing()
            }

            try {
                task.await()
            } catch (e: Exception) {
                println("Deferred cancelled due to ${e.message}")
            }
            println("Completed")
        }
    }

    private fun doSomeThing() {
        throw UnsupportedOperationException("Can't do")
    }
}
