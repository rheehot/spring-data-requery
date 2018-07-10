package org.springframework.data.requery.kotlin.domain.sample

import io.requery.*
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Category
 *
 * @author debop
 */
@Entity
interface Category: PersistableObject {

    @get:Key
    @get:Generated
    val id: Long

    @get:ForeignKey
    @get:ManyToOne
    var product: Product?

}