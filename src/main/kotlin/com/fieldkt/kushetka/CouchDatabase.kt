package com.fieldkt.kushetka

import io.ktor.client.request.delete
import io.ktor.client.request.head
import io.ktor.client.request.put
import io.ktor.http.URLBuilder
import mu.KotlinLogging.logger


class CouchDatabase(
    val name: String,
    val server: CouchDbServer,
    val transformer: DocumentTransformer = DocumentTransformerImpl()
) {

    suspend fun create(): Boolean {
        server.httpClient.put<Unit>(URLBuilder(server.baseUrl).path(name).build())
        return true
    }

    suspend fun drop(): Boolean {
        server.httpClient.delete<Unit>(URLBuilder(server.baseUrl).path(name).build())
        return true
    }

    suspend fun exists(): Boolean {
        return runCatching { server.httpClient.head<Unit>(URLBuilder(server.baseUrl).path(name).build()) }.isSuccess
    }

    inline fun <reified T : Any> document(id: String): CouchDocumentReference<T> {
        return CouchDocumentReference(this, id, T::class.java)
    }

    companion object {
        val log = logger {}
    }
}
