package org.springframework.data.requery.kotlin.domain.blob

import io.requery.*
import org.springframework.data.requery.kotlin.converters.ByteArrayToBlobConverter
import java.io.Serializable

/**
 * org.springframework.data.requery.kotlin.domain.blob.BinaryData
 *
 * @author debop
 */
@Entity
interface BinaryData: Persistable, Serializable {

    @get:Key
    @get:Generated
    val id: Int

    @get:Column(nullable = false)
    var name: String

    @get:Lazy
    @get:Convert(ByteArrayToBlobConverter::class)
    var picture: ByteArray

}