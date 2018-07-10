package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Item
 *
 * @author debop
 */
@Entity
interface Item: PersistableObject {

    @get:Key
    var id: Int

    @get:Key
    var manufacturerId: Int
}