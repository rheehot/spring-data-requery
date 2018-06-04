package com.coupang.springframework.data.requery.domain.blob

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * com.coupang.springframework.data.requery.domain.blob.BinaryDataTest
 * @author debop
 * @since 18. 5. 31
 */
class BinaryDataTest: AbstractDomainTest() {


    @Test
    fun `deal blob property`() {
        val bytes = ByteArray(8192)
        rnd.nextBytes(bytes)

        val binData = BinaryData().apply {
            name = "binary data"
            picture = bytes
        }

        requeryTemplate.insert(binData)

        val loaded = requeryTemplate.findById(BinaryData::class.java, binData.id)!!
        assertThat(loaded.id).isEqualTo(binData.id)
        assertThat(loaded.picture).isNotNull().isEqualTo(bytes)
    }
}