package com.fieldkt.kushetka

import java.net.URL


class CouchDbServer internal constructor(
    val baseUrl: URL,
    val credentials: Credentials?
) {
    data class Credentials(val username: String, val password: String)

    fun getDb(name: String) = CouchDatabase(name, this)
}
