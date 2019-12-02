package com.fieldkt.kushetka

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.host
import io.ktor.client.request.port
import io.ktor.client.request.put
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.util.KtorExperimentalAPI
import mu.KotlinLogging.logger


class CouchDbServer internal constructor(
    val baseUrl: Url,
    val credentials: Credentials?
) {
    data class Credentials(val username: String, val password: String)

    @UseExperimental(KtorExperimentalAPI::class)
    val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                registerKotlinModule()
            }
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.NONE
        }
        defaultRequest {
            host = baseUrl.host
            port = baseUrl.port
        }
        credentials?.also { (user, pass) ->
            install(Auth) {
                basic {
                    username = user
                    password = pass
                    sendWithoutRequest = true
                }
            }
        }
    }

    fun db(name: String): CouchDatabase = CouchDatabase(name, this)




    companion object {
        val log = logger {}
    }

//    internal suspend inline fun <T, reified U> get(uri: URI, body: T?): U = sendRequest(HttpMethod.Get, uri, body)
//    private suspend inline fun <T, reified U> sendRequest(method: HttpMethod, path: URI, body: T?): U {
//        return httpClient.request(URLBuilder(baseUrl).path().build()) {
//            this.method = method
//            if (body != null) {
//                this.body = body
//            }
//        }
//    }
}
