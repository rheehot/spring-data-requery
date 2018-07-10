package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Customer
 *
 * @author debop
 */
@Entity
interface Customer: PersistableObject {

    @get:Key
    var id: Long

    var name: String
}