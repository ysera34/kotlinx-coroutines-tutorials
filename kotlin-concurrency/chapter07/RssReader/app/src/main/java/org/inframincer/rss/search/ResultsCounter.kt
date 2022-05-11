package org.inframincer.rss.search

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.channels.actor

object ResultsCounter {
    private val context = newSingleThreadContext("counter")
    private var counter = 0
    private val notifications = Channel<Int>(Channel.CONFLATED)

    private val actor = GlobalScope.actor<Action>(context) {
        for (message in channel) {
            when (message) {
                Action.INCREASE -> counter++
                Action.RESET -> counter = 0
            }
            notifications.send(counter )
        }
    }

    suspend fun increment() = actor.send(Action.INCREASE)

    suspend fun reset() = actor.send(Action.RESET)

    fun getNotificationChannel(): ReceiveChannel<Int> = notifications
}

enum class Action {
    INCREASE,
    RESET,
}
