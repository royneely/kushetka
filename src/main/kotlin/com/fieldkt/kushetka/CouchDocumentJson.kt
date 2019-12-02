package com.fieldkt.kushetka

import com.fasterxml.jackson.annotation.JsonProperty

data class CouchDocumentJson<T>(
    val obj: T,
    @JsonProperty(value = "_rev")
    val rev: String,
    @JsonProperty(value = "_id")
    val id: String
)
