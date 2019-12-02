/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.fieldkt.kushetka

import com.fieldkt.kushetka.CouchDbServer.Credentials
import io.ktor.http.Url

object Kushetka {
    fun getServer(baseUrl: String, credentials: Credentials? = null): CouchDbServer {
        return getServer(Url(baseUrl), credentials)
    }

    fun getServer(baseUrl: Url, credentials: Credentials? = null): CouchDbServer {
        return CouchDbServer(baseUrl, credentials)
    }
}
