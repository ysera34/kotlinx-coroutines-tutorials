package org.inframincer.rss

import kotlinx.coroutines.runBlocking
import org.junit.Test

class SuspendFunctionTest {

    @Test
    fun suspendFetchByIdTest() {
        runBlocking {
            val client = ProfileSuspendServiceClient()
            val profile = client.fetchById(12)
            println(profile)
        }
    }
}

interface ProfileSuspendServiceRepository {
    suspend fun fetchByName(name: String): Profile
    suspend fun fetchById(id: Long): Profile
}

class ProfileSuspendServiceClient : ProfileSuspendServiceRepository {
    override suspend fun fetchByName(name: String): Profile {
        return Profile(1, name, 28)
    }

    override suspend fun fetchById(id: Long): Profile {
        return Profile(id, "Susan", 28)
    }
}
