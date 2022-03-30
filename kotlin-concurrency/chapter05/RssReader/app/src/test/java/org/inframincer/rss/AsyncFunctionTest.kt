package org.inframincer.rss

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AsyncFunctionTest {

    @Test
    fun asyncFetchByIdTest() {
        runBlocking {
            val client = ProfileAsyncServiceClient()
            val profile = client.asyncFetchById(12).await()
            println(profile)
        }
    }
}

interface ProfileAsyncServiceRepository {
    fun asyncFetchByName(name: String): Deferred<Profile>
    fun asyncFetchById(id: Long): Deferred<Profile>
}

class ProfileAsyncServiceClient : ProfileAsyncServiceRepository {
    override fun asyncFetchByName(name: String): Deferred<Profile> {
        return GlobalScope.async {
            Profile(1, name, 28)
        }
    }

    override fun asyncFetchById(id: Long): Deferred<Profile> {
        return GlobalScope.async {
            Profile(id, "Susan", 28)
        }
    }
}
