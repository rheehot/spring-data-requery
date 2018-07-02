package org.springframework.data.requery.converters

import io.requery.Converter

/**
 * [List<Int>] 를 String 으로 저장하는 [Converter] 입니다.
 *
 * @author debop
 * @since 18. 7. 2
 */
class IntArrayListToStringConverter: Converter<List<Int>, String> {

    override fun getPersistedType(): Class<String> = String::class.java

    @Suppress("UNCHECKED_CAST")
    override fun getMappedType(): Class<List<Int>> = List::class.java as Class<List<Int>>

    override fun getPersistedSize(): Int? = null

    override fun convertToMapped(type: Class<out List<Int>>?, value: String?): List<Int>? {
        return value?.let {
            val items = it.split(",")
            items.map { it.toInt() }.toMutableList()
        }
    }

    override fun convertToPersisted(value: List<Int>?): String? {
        return value?.joinToString(",") { it.toString() }
    }
}