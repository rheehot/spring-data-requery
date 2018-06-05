package com.coupang.springframework.data.requery.convert

import io.requery.Converter

/**
 * IntArrayListStringConverter
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class IntArrayStringConverter: Converter<IntArray, String> {

    override fun convertToMapped(type: Class<out IntArray>?, value: String?): IntArray? {
        return value?.let {
            val items = it.split(",")
            items.map { it.toInt() }.toIntArray()
        }
    }

    override fun convertToPersisted(value: IntArray?): String? {
        return value?.joinToString(",")
    }

    override fun getPersistedType(): Class<String> = String::class.java

    override fun getMappedType(): Class<IntArray> = IntArray::class.java

    override fun getPersistedSize(): Int? = null
}