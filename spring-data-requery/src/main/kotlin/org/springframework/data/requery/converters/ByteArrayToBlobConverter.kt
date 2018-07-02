package org.springframework.data.requery.converters

import io.requery.Converter
import java.sql.Blob
import javax.sql.rowset.serial.SerialBlob

/**
 * [ByteArray]를 [Blob]로 저장하도록 하는 Converter
 *
 * @author debop
 * @since 18. 7. 2
 */
class ByteArrayToBlobConverter: Converter<ByteArray, Blob> {

    override fun getPersistedType(): Class<Blob> = Blob::class.java

    override fun getMappedType(): Class<ByteArray> = ByteArray::class.java

    override fun getPersistedSize(): Int? = null

    override fun convertToMapped(type: Class<out ByteArray>?, value: Blob?): ByteArray? {
        return value?.binaryStream?.readBytes()
    }

    override fun convertToPersisted(value: ByteArray?): Blob? {
        return value?.let { SerialBlob(it) }
    }

}