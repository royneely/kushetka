package com.fieldkt.kushetka

class CouchDatabase(
    private val name: String,
    private val server: CouchDbServer
) {

    private val map = mutableMapOf<String, Any>()


    fun <T : Any> insert(obj: T, id: String) {
        map[id] = obj
    }


    fun <T : Any> findById(id: String): T? {
        @Suppress("UNCHECKED_CAST")
        return map[id] as T?
    }
}
