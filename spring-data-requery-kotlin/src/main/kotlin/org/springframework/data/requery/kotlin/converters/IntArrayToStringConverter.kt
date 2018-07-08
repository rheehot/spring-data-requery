package org.springframework.data.requery.kotlin.converters

import io.requery.Converter

/**
 * [IntArray] 를 String 으로 저장하는 [Converter] 입니다.
 *
 * @author debop
 * @since 18. 7. 2
 */
class IntArrayToStringConverter: Converter<IntArray, String> {

    override fun getPersistedType(): Class<String> = String::class.java

    override fun getMappedType(): Class<IntArray> = IntArray::class.java

    override fun getPersistedSize(): Int? = null


    override fun convertToMapped(type: Class<out IntArray>?, value: String?): IntArray? {
        return value?.let {
            it.split(",").map { it.toInt() }.toIntArray()
        }
    }

    override fun convertToPersisted(value: IntArray?): String? {
        return value?.let {
            it.joinToString(",") { it.toString() }
        }
    }
}