package org.inframincer.rss

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutineExceptionHandlerTest {

    @Test
    fun coroutineExceptionHandlerTest() {
        runBlocking {
            val handler = CoroutineExceptionHandler { context, throwable ->
                println("Error captured in $context")
                println("Message: ${throwable.message}")
            }

            GlobalScope.launch(handler) {
                TODO("Not implemented yet!")
            }

            delay(500)
        }
    }

    /**
     * still running
     * still running
     * cancelled, will end now
     * Took 1246 ms
     */
    @Test
    fun nonCancellableTest1() {
        runBlocking {
            val duration = measureTimeMillis {
                val job = launch {
                    try {
                        while (isActive) {
                            delay(500)

                            println("still running")
                        }
                    } finally {
                        println("cancelled, will end now")
                    }
                }

                delay(1200)
                job.cancelAndJoin()
            }

            println("Took $duration ms")
        }
    }

    /**
     * still running
     * still running
     * cancelled, will delay finalization now
     * Took 1238 ms
     */
    @Test
    fun nonCancellableTest2() {
        runBlocking {
            val duration = measureTimeMillis {
                val job = launch {
                    try {
                        while (isActive) {
                            delay(500)

                            println("still running")
                        }
                    } finally {
                        println("cancelled, will delay finalization now")
                        delay(1000)
                        println("delay completed, bye bye")
                    }
                }

                delay(1200)
                job.cancelAndJoin()
            }

            println("Took $duration ms")
        }
    }

    /**
     * still running
     * still running
     * cancelled, will delay finalization now
     * delay completed, bye bye
     * Took 2251 ms
     */
    @Test
    fun nonCancellableTest3() {
        runBlocking {
            val duration = measureTimeMillis {
                val job = launch {
                    try {
                        while (isActive) {
                            delay(500)

                            println("still running")
                        }
                    } finally {
                        withContext(NonCancellable) {
                            println("cancelled, will delay finalization now")
                            delay(1000)
                            println("delay completed, bye bye")
                        }
                    }
                }

                delay(1200)
                job.cancelAndJoin()
            }

            println("Took $duration ms")
        }
    }
}
