package com.coupang.springframework.data.requery.domain.blob

import com.coupang.kotlinx.data.requery.conveters.ByteArrayBlobConverter
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * com.coupang.springframework.data.requery.domain.blob.AbstractBinaryData
 * @author debop
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractBinaryData: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column(nullable = false)
    abstract var name: String

    @get:Convert(ByteArrayBlobConverter::class)
    abstract var picture: ByteArray?

    override fun hashCode(): Int {
        return name.hashCode()
    }
}