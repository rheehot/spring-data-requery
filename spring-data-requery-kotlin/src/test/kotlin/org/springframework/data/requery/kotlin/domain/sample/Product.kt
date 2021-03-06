package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Product
 *
 * @author debop
 */
@Entity
interface Product: PersistableObject {

    @get:Key
    @get:Generated
    val id: Long

    var name: String
}