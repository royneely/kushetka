package com.fieldkt.kushetka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class DocumentTransformerImpl(val om: ObjectMapper = jacksonObjectMapper()) :
    DocumentTransformer {
    override suspend fun <T : Any> fromJson(json: String, clazz: Class<T>): CouchDocumentJson<T> {
        val tree = om.readTree(json)
        return CouchDocumentJson(
            om.readValue(json, clazz),
            tree.get("_rev").asText(),
            tree.get("_id").asText()
        )
    }

    override suspend fun <T : Any> toJson(obj: T): String {
        return om.writeValueAsString(obj)
    }
}
