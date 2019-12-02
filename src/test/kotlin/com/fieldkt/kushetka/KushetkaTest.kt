package com.fieldkt.kushetka

import com.fieldkt.kushetka.CouchDbServer.Credentials
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging.logger
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KushetkaTest {
    @Test
    fun constructDatabaseAndInsert() {
        val log = logger {}
        val couchServer = Kushetka.getServer("http://localhost:5984", Credentials("test-user", "test-pass"))
        val petsDb = couchServer.db("pets")
        runBlocking { petsDb.reset() }

        listOf(
            Cat("lucy", 45, true),
            Cat("mittens", 87, true),
            Cat("george", 52, false),
            Dog("rover", 45, 88),
            Dog("doug", 87, 77),
            Dog("rex", 52, 89)
        ).forEach { cat ->
            runBlocking { petsDb.document<Animal>(cat.id).insert(cat) }
        }

        runBlocking {
            val (mittens, rev) = petsDb.document<Cat>("mittens").get()
                ?: error("mittens could not be found")

            log.info { "mittens = $mittens" }

            assertTrue(mittens.lazinessScore == 87)
            assertNotNull(rev)
        }

        val roverDoc = petsDb.document<Dog>("rover")

        runBlocking {
            val (rover, rev) = roverDoc.get() ?: error("rover could not be found")
            log.info { "rover insert = $rover" }
            assertTrue(rover.lazinessScore == 45)
            assertTrue(rover.excitement == 88)
            assertNotNull(rev)
        }
        runBlocking {
            roverDoc.get()?.also { (rover, rev) ->
                roverDoc.upsert(rover.copy(excitement = 96), rev)
            }
            val (rover, rev) = roverDoc.get() ?: error("rover could not be found")
            log.info { "rover upsert = $rover" }
            assertTrue(rover.lazinessScore == 45)
            assertTrue(rover.excitement == 96)
            assertNotNull(rev)
        }
        runBlocking {
            roverDoc.update { it.copy(lazinessScore = 77) }
            val (rover, rev) = roverDoc.get() ?: error("rover could not be found")
            log.info { "rover upsert = $rover" }
            assertTrue(rover.lazinessScore == 77)
            assertTrue(rover.excitement == 96)
            assertNotNull(rev)
        }
    }

    companion object {
        suspend fun CouchDatabase.reset() {
            if (exists()) drop()
            create()
        }
    }
}
