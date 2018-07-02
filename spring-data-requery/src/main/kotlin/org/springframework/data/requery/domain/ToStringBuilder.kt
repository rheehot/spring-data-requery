package org.springframework.data.requery.domain

import java.io.Serializable
import java.util.*

/**
 * org.springframework.data.requery.domain.ToStringBuilder
 *
 * @author debop
 */
class ToStringBuilder(val className: String): Serializable {

    companion object {
        @JvmStatic fun of(className: String): ToStringBuilder = ToStringBuilder(className)
        @JvmStatic fun of(obj: Any): ToStringBuilder = ToStringBuilder(obj)
    }

    constructor(obj: Any): this(obj::class.java.simpleName)

    init {
        check(className.isNotBlank()) { "className must not be null or empty." }
    }

    private val map = HashMap<String, Any?>()
    private var cachedToString: String? = null

    private fun toStringValue(limit: Int): String {
        if(cachedToString == null) {
            val props = map.entries
                .joinToString(separator = ",", limit = limit) {
                    "${it.key}=${it.value.asString()}"
                }
            cachedToString = "$className($props)"
        }
        return cachedToString!!
    }

    private fun Any?.asString(): String = this?.toString() ?: "<null>"

    fun add(name: String, value: Any?): ToStringBuilder = apply {
        map[name] = value?.toString() ?: ""
    }

    override fun toString(): String = toStringValue(-1)

    fun toString(limit: Int): String = toStringValue(limit)

}