package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.SimpleEntity
 *
 * @author debop
 */
@Entity
interface SimpleEntity: PersistableObject {

    @get:Key
    var first: String

    @get:Key
    var second: String

}