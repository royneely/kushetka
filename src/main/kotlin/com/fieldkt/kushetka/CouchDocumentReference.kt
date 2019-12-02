package com.fieldkt.kushetka

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.http.ContentType.Application
import io.ktor.http.URLBuilder
import io.ktor.http.content.TextContent
import java.time.Duration


class CouchDocumentReference<T : Any>(
    private val db: CouchDatabase,
    private val id: String,
    private val type: Class<T>
) {

    suspend fun insert(obj: T) {
        db.server.httpClient.put<Unit>(URLBuilder(db.server.baseUrl).path(db.name, id).build()) {
            body = TextContent(db.transformer.toJson(obj), Application.Json)
        }
    }

    suspend fun upsert(obj: T, rev: String?) {
        db.server.httpClient.put<Unit>(URLBuilder(db.server.baseUrl).path(db.name, id).build()) {
            if (rev != null) {
                parameter("rev", rev)
            }
            body = TextContent(db.transformer.toJson(obj), Application.Json)
        }
    }

    suspend fun update(updater: suspend (T) -> T): T {
        val (original, rev) = get()!!
        val updated = updater(original)
        upsert(updated, rev)
        return updated
    }

    suspend fun get(): CouchDocumentJson<T>? {
        return db.server.httpClient.get<String>(URLBuilder(db.server.baseUrl).path(db.name, id).build())
            .let { db.transformer.fromJson(it, type) }
    }
}
