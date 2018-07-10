package org.springframework.data.requery.kotlin.coroutines

import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withTimeout
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletionStage
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.stream.Stream
import kotlin.coroutines.experimental.CoroutineContext

fun <T: Any?> T.toDeferred(context: CoroutineContext = DefaultDispatcher): Deferred<T> {
    return async(context) { this@toDeferred }
}

fun <T> Future<T>.asDeferred(context: CoroutineContext = DefaultDispatcher): Deferred<T> {
    return async(context) { this@asDeferred.get() }
}

fun <T> CompletionStage<T>.toDeferred(context: CoroutineContext = DefaultDispatcher): Deferred<T> {
    return async(context) { this@toDeferred.toCompletableFuture().join() }
}

fun <T> CompletionStage<T>.asDeferred(context: CoroutineContext = DefaultDispatcher): Deferred<T> {
    return async(context) { this@asDeferred.toCompletableFuture().join() }
}

suspend fun <T> Iterable<Deferred<T>>.flatten(context: CoroutineContext = DefaultDispatcher): Deferred<List<T>> {
    return this@flatten.map { it.await() }.toDeferred(context)
}

fun <T> Stream<Deferred<T>>.flatten(context: CoroutineContext = DefaultDispatcher): Deferred<Stream<T>> {
    return async(context) {
        this@flatten.map {
            it.getCompleted()
        }
    }
}

suspend fun <T: Any?> Deferred<T>.await(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): T =
    withTimeout(time, unit) { await() }

@Suppress("UNCHECKED_CAST")
suspend fun <T: Any?> Deferred<T>.awaitSilence(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): T =
    try {
        withTimeout(time, unit) { await() }
    } catch(e: Exception) {
        null as T
    }

suspend fun <T: Any?> Deferred<T>.awaitOrDefault(defaultValue: T, time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): T =
    try {
        withTimeout(time, unit) { await() }
    } catch(e: CancellationException) {
        defaultValue
    }