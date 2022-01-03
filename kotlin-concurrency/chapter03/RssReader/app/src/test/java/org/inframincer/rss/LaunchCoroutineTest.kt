package org.inframincer.rss

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class LaunchCoroutineTest {

    @Test
    fun launchTest1() {
        runBlocking {
            val task = GlobalScope.launch {
                doSomeThing()
            }
            task.join()
            println("Completed")
        }
    }

    @Test
    fun launchTest2() {
        runBlocking {
            val task = GlobalScope.launch(start = CoroutineStart.LAZY) {
                doSomeThing()
            }
            delay(2000)
        }
    }

    @Test
    fun launchTest3() {
        runBlocking {
            val task = GlobalScope.launch(start = CoroutineStart.LAZY) {
                delay(2000)
                println("Task Completed")
            }
            println("Before start")
            task.start()
            println("Completed")
        }
    }

    @Test
    fun launchTest4() {
        runBlocking {
            val task = GlobalScope.launch(start = CoroutineStart.LAZY) {
                delay(2000)
                println("Task Completed")
            }
            println("Before join")
            task.join()
            println("Completed")
        }
    }

    @Test
    fun launchTest5() {
        runBlocking {
            val task = GlobalScope.launch(start = CoroutineStart.LAZY) {
                delay(2000)
                println("Task Completed")
            }
            println("Before cancel")
            task.cancel()
            println("cancelled")
        }
    }

    @InternalCoroutinesApi
    @Test
    fun launchTest6() {
        runBlocking {
            val task = GlobalScope.launch {
                delay(2000)
                println("Task Completed")
            }
            println("Before cancel")
            delay(1000)
            task.cancel()
            println("cancelled")

            val cancellation = task.getCancellationException()
            println(cancellation.message)
        }
    }

    @Test
    fun launchTest7() {
        runBlocking {
            val exceptionHandler =
                CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
                    println("Job cancelled due to ${throwable.message}")
                }
            val task = GlobalScope.launch(exceptionHandler) {
                doSomeThing()
                println("Task Completed")
            }
            println("Before cancel")
            delay(1000)
            task.cancel()
            println("cancelled")
        }
    }

    @Test
    fun launchTest8() {
        runBlocking {
            val task = GlobalScope.launch {
                doSomeThing()
                println("Task Completed")
            }.invokeOnCompletion { cause ->
                cause?.let {
                    println("Job cancelled due to ${it.message}")
                }
            }
            delay(1000)
        }
    }

    @Test
    fun launchTest9() {
        runBlocking {
            val time = measureTimeMillis {
                val task = GlobalScope.launch {
                    delay(2000)
                    println("Task Completed")
                }
                // Wait for it to complete once
                task.join()

                // Restart the Job
                task.start()
                task.join()
            }
            println("Took $time ms")
        }
    }

    private fun doSomeThing() {
        throw UnsupportedOperationException("Can't do")
    }
}
