package org.springframework.data.requery.kotlin.domain.blob

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest

/**
 * org.springframework.data.requery.kotlin.domain.blob.BinaryDataTest
 *
 * @author debop
 */
class BinaryDataTest: AbstractDomainTest() {

    @Test
    fun `save byte array data`() {

        val bytes = ByteArray(8192)
        rnd.nextBytes(bytes)

        val binData = BinaryDataEntity().apply {
            name = "binary data"
            picture = bytes
        }

        operations.insert(binData)
        assertThat(binData.id).isNotNull()

        val saved = operations.findById(BinaryData::class, binData.id)!!
        assertThat(saved.id).isEqualTo(binData.id)
        assertThat(saved.name).isEqualTo(binData.name)
        assertThat(saved.picture).isNotNull().isEqualTo(binData.picture)

        rnd.nextBytes(bytes)
        saved.picture = bytes
        operations.upsert(saved)
    }
}