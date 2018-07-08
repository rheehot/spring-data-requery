package org.springframework.data.requery.kotlin.provider

/**
 * org.springframework.data.requery.provider.ProxyIdAccessor
 *
 * @author debop
 */
interface ProxyIdAccessor {

    fun shouldUseAccessorFor(entity: Any?): Boolean

    fun getIdentifierFrom(entity: Any?): Any?
}