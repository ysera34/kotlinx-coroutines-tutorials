package org.inframincer.rss

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

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

    private fun doSomeThing() {
        throw UnsupportedOperationException("Can't do")
    }
}
