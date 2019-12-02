package com.fieldkt.kushetka

import kotlin.reflect.KClass

interface DocumentTransformer {
    suspend fun <T : Any> fromJson(json: String, clazz: Class<T>): CouchDocumentJson<T>
    suspend fun <T : Any> fromJson(json: String, clazz: KClass<T>): CouchDocumentJson<T> =
        fromJson(json, clazz.java)

    suspend fun <T : Any> toJson(obj: T): String
}
