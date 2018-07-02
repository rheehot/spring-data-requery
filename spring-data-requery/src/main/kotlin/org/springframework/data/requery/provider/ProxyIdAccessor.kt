package org.springframework.data.requery.provider

/**
 * org.springframework.data.requery.provider.ProxyIdAccessor
 *
 * @author debop
 */
@Deprecated("사용할 필요 없다")
interface ProxyIdAccessor {

    fun shouldUseAccessorFor(entity: Any?): Boolean

    fun getIdentifierFrom(entity: Any?): Any?
}