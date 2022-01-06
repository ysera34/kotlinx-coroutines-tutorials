package org.inframincer.rss

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutineStateTest {

    @Test
    fun stateTest1() {
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
}
