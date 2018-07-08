package org.springframework.data.requery.kotlin.converters

import io.requery.Converter

/**
 * [List<Int>] 를 String 으로 저장하는 [Converter] 입니다.
 *
 * @author debop
 * @since 18. 7. 2
 */
class IntArrayListToStringConverter: Converter<ArrayList<Int>, String> {

    override fun getPersistedType(): Class<String> = String::class.java

    @Suppress("UNCHECKED_CAST")
    override fun getMappedType(): Class<ArrayList<Int>> = ArrayList::class.java as Class<ArrayList<Int>>

    override fun getPersistedSize(): Int? = null

    override fun convertToMapped(type: Class<out ArrayList<Int>>?, value: String?): ArrayList<Int>? {
        return value?.let {
            val items = it.split(",")
            items.map { it.toInt() }.toMutableList() as ArrayList<Int>
        }
    }

    override fun convertToPersisted(value: ArrayList<Int>?): String? {
        return value?.joinToString(",") { it.toString() }
    }
}