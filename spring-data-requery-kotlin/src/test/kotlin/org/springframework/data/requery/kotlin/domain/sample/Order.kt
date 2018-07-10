package org.springframework.data.requery.kotlin.domain.sample

import io.requery.*
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Order
 *
 * @author debop
 */
@Entity
@Table(name = "ORDERS")
interface Order: PersistableObject {

    @get:Key
    @get:Generated
    val id: Long

    var orderNo: String

    @get:ForeignKey
    @get:ManyToOne
    var customer: Customer?
}