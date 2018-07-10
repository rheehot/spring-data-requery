package org.springframework.data.requery.kotlin.coroutines

import io.requery.query.Result
import io.requery.query.ResultDelegate
import io.requery.query.element.QueryElement
import io.requery.query.element.QueryWrapper
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

/**
 * Kotlin Coroutines를 이용한 비동기 결과를 표현합니다.
 *
 * @author debop
 * @since 18. 5. 16
 */
class DeferredResult<E>(delegate: Result<E>,
                        val coroutineDispatcher: CoroutineDispatcher = DefaultDispatcher)
    : ResultDelegate<E>(delegate), QueryWrapper<E> {

    inline fun <V> toDefered(crossinline block: DeferredResult<E>.() -> V): Deferred<V> {
        return async(coroutineDispatcher) { block() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun unwrapQuery(): QueryElement<E> {
        return (delegate as QueryWrapper<E>).unwrapQuery()
    }
}