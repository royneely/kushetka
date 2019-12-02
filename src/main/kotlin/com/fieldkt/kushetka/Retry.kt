package com.fieldkt.kushetka

import kotlinx.coroutines.delay
import java.time.Duration


suspend fun <T> retry(retryPolicy: Iterator<Duration>, block: suspend () -> T): T {
    while (true) {
        try {
            return block()
        } catch (t: Throwable) {
            if (retryPolicy.hasNext()) {
                delay(retryPolicy.next().toMillis())
            } else throw t
        }
    }
}

open class RetryPolicy(iterator: Iterator<Duration>) : Iterator<Duration> by iterator {
    constructor(collection: Collection<Duration>) : this(collection.iterator())

    class MaxRetriesConstantDelay(maxRetries: Int, delay: Duration) : RetryPolicy((1..maxRetries).map { delay })
}
