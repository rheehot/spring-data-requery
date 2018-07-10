package org.springframework.data.requery.kotlin.coroutines

import io.requery.query.Scalar
import io.requery.query.ScalarDelegate
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.Deferred

/**
 * Kotlin Coroutines 을 이용하여 Scala 수형의 질의 반환 값을 받을 때 사용하는 클래스입니다.
 *
 * @author debop
 * @since 18. 5. 16
 */
class DeferredScalar<E>(delegate: Scalar<E>,
                        private val coroutineDispatcher: CoroutineDispatcher = DefaultDispatcher)
    : ScalarDelegate<E>(delegate) {

    fun toDeferred(): Deferred<E> =
        toCompletableFuture().toDeferred(coroutineDispatcher)

    fun toDeferred(dispatcher: CoroutineDispatcher): Deferred<E> =
        toCompletableFuture().toDeferred(dispatcher)

}