package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Dummy
 *
 * @author debop
 */
@Entity
interface Dummy: PersistableObject {

    @get:Key
    @get:Generated
    val id: Int

    var name: String
}